package dev.amal.service

import dev.amal.data.requests.FollowUpdateRequest
import dev.amal.data.repository.follow.FollowRepository

class FollowService(
    private val followRepository: FollowRepository
) {
    suspend fun followUserIfExists(request: FollowUpdateRequest, followingUserId: String): Boolean =
        followRepository.followUserIfExists(followingUserId, request.followedUserId)

    suspend fun unfollowUserIfExists(followedUserId: String, followingUserId: String): Boolean =
        followRepository.unfollowUserIfExists(followingUserId, followedUserId)
}