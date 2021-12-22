package dev.amal.data.repository.follow

import dev.amal.data.models.Following
import dev.amal.data.models.User
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class FollowRepositoryImpl(
    db: CoroutineDatabase
) : FollowRepository {

    private val following = db.getCollection<Following>()
    private val users = db.getCollection<User>()

    override suspend fun followUserIfExists(
        followingUserId: String,
        followedUserId: String
    ): Boolean {
        val doesFollowingUserExist = users.findOneById(followingUserId) != null
        val doesFollowedUserExist = users.findOneById(followedUserId) != null
        if (!doesFollowingUserExist || !doesFollowedUserExist) {
            return false
        }
        following.insertOne(
            Following(followingUserId, followedUserId)
        )
        return true
    }

    override suspend fun unfollowUserIfExists(
        followingUserId: String,
        followedUserId: String
    ): Boolean {
        val deleteResult = following.deleteOne(
            and(
                Following::followingUserId eq followingUserId,
                Following::followedUserId eq followedUserId,
            )
        )
        return deleteResult.deletedCount > 0
    }

    override suspend fun doesUserFollow(
        followingUserId: String, followedUserId: String
    ): Boolean = following.findOne(
        and(
            Following::followingUserId eq followingUserId,
            Following::followedUserId eq followedUserId
        )
    ) != null


    override suspend fun getFollowsByUser(userId: String): List<Following> =
        following.find(Following::followingUserId eq userId).toList()

}