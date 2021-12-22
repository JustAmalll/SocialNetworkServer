package dev.amal.service

import dev.amal.data.models.Post
import dev.amal.data.repository.post.PostRepository
import dev.amal.data.requests.CreatePostRequest
import dev.amal.data.responses.PostResponse
import dev.amal.util.Constants

class PostService(
    private val repository: PostRepository
) {

    suspend fun createPost(
        request: CreatePostRequest, userId: String, imageUrl: String
    ): Boolean = repository.createPost(
        Post(
            imageUrl = imageUrl,
            userId = userId,
            timestamp = System.currentTimeMillis(),
            description = request.description
        )
    )

    suspend fun getPostsForFollows(
        ownUserId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): List<PostResponse> =
        repository.getPostsByFollows(ownUserId, page, pageSize)

    suspend fun getPostsForProfile(
        ownUserId: String,
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): List<PostResponse> =
        repository.getPostsForProfile(ownUserId, userId, page, pageSize)

    suspend fun getPost(postId: String): Post? = repository.getPost(postId)

    suspend fun getPostDetails(ownUserId: String, postId: String): PostResponse? =
        repository.getPostDetails(ownUserId, postId)

    suspend fun deletePost(postId: String) {
        repository.deletePost(postId)
    }
}