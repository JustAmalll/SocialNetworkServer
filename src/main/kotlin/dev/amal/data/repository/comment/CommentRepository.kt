package dev.amal.data.repository.comment

import dev.amal.data.models.Comment
import dev.amal.data.responses.CommentResponse

interface CommentRepository {
    suspend fun createComment(comment: Comment): String
    suspend fun deleteComment(commentId: String): Boolean
    suspend fun deleteCommentsFromPost(postId: String): Boolean
    suspend fun getCommentsForPost(postId: String, ownUserId: String): List<CommentResponse>
    suspend fun getComment(commentId: String): Comment?
}