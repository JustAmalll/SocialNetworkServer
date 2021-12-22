package dev.amal.service

import dev.amal.data.models.User
import dev.amal.data.repository.follow.FollowRepository
import dev.amal.data.repository.user.UserRepository
import dev.amal.data.requests.CreateAccountRequest
import dev.amal.data.requests.UpdateProfileRequest
import dev.amal.data.responses.ProfileResponse
import dev.amal.data.responses.UserResponseItem
import dev.amal.util.Constants

class UserService(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) {

    suspend fun doesUserWithEmailExist(email: String): Boolean =
        userRepository.getUserByEmail(email) != null

    suspend fun getUserProfile(userId: String, callerUserId: String): ProfileResponse? {
        val user = userRepository.getUserById(userId) ?: return null
        return ProfileResponse(
            userId = user.id,
            username = user.username,
            bio = user.bio,
            followerCount = user.followerCount,
            followingCount = user.followingCount,
            postCount = user.postCount,
            profilePictureUrl = user.profileImageUrl,
            bannerUrl = user.bannerUrl,
            topSkills = user.skills,
            gitHubUrl = user.gitHubUrl,
            instagramUrl = user.instagramUrl,
            linkedInUrl = user.linkedInUrl,
            isOwnProfile = userId == callerUserId,
            isFollowing = if (userId != callerUserId) {
                followRepository.doesUserFollow(callerUserId, userId)
            } else false
        )
    }

    suspend fun getUserByEmail(email: String): User? =
        userRepository.getUserByEmail(email)

    fun isValidPassword(enteredPassword: String, actualPassword: String): Boolean =
        enteredPassword == actualPassword

    suspend fun updateUser(
        userId: String,
        profileImageUrl: String?,
        bannerUrl: String?,
        updateProfileRequest: UpdateProfileRequest
    ): Boolean =
        userRepository.updateUser(userId, profileImageUrl, bannerUrl, updateProfileRequest)

    suspend fun searchForUsers(query: String, userId: String): List<UserResponseItem> {
        val users = userRepository.searchForUsers(query)
        val followsByUser = followRepository.getFollowsByUser(userId)
        return users.map { user ->
            val isFollowing = followsByUser.find { it.followedUserId == user.id } != null
            UserResponseItem(
                userId = user.id,
                username = user.username,
                profilePictureUrl = user.profileImageUrl,
                bio = user.bio,
                isFollowing = isFollowing
            )
        }.filter { it.userId != userId }
    }

    suspend fun createUser(request: CreateAccountRequest) {
        userRepository.createUser(
            User(
                email = request.email,
                username = request.username,
                password = request.password,
                profileImageUrl = Constants.DEFAULT_PROFILE_PICTURE_PATH,
                bannerUrl = Constants.DEFAULT_BANNER_IMAGE_PATH,
                bio = "",
                gitHubUrl = null,
                instagramUrl = null,
                linkedInUrl = null
            )
        )
    }

    fun validateCreateAccountRequest(request: CreateAccountRequest): ValidationEvent {
        if (request.email.isBlank() || request.password.isBlank() || request.username.isBlank()) {
            return ValidationEvent.ErrorFieldEmpty
        }
        return ValidationEvent.Success
    }

    sealed class ValidationEvent {
        object ErrorFieldEmpty : ValidationEvent()
        object Success : ValidationEvent()
    }
}