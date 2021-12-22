package dev.amal.service

import dev.amal.data.models.Comment
import dev.amal.data.repository.comment.CommentRepository
import dev.amal.data.repository.user.UserRepository
import dev.amal.data.requests.CreateCommentRequest
import dev.amal.data.responses.CommentResponse
import dev.amal.util.Constants

class CommentService(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository
) {
    suspend fun createComment(
        createCommentRequest: CreateCommentRequest,
        userId: String
    ): ValidationEvent {
        createCommentRequest.apply {
            if (comment.isBlank() || postId.isBlank())
                return ValidationEvent.ErrorFieldEmpty
            if (comment.length > Constants.MAX_COMMENT_LENGTH)
                return ValidationEvent.ErrorCommentTooLong
        }
        val user = userRepository.getUserById(userId) ?: return ValidationEvent.UserNotFound
        commentRepository.createComment(
            Comment(
                username = user.username,
                profileImageUrl = user.profileImageUrl,
                likeCount = 0,
                comment = createCommentRequest.comment,
                userId = userId,
                postId = createCommentRequest.postId,
                timestamp = System.currentTimeMillis()
            )
        )
        return ValidationEvent.Success
    }

    suspend fun getCommentsForPost(postId: String, ownUserId: String): List<CommentResponse> =
        commentRepository.getCommentsForPost(postId, ownUserId)

    suspend fun getCommentById(commentId: String): Comment? =
        commentRepository.getComment(commentId)

    suspend fun deleteComment(commentId: String): Boolean =
        commentRepository.deleteComment(commentId)

    suspend fun deleteCommentsForPost(postId: String) {
        commentRepository.deleteCommentsFromPost(postId)
    }

    sealed class ValidationEvent {
        object ErrorFieldEmpty : ValidationEvent()
        object ErrorCommentTooLong : ValidationEvent()
        object UserNotFound : ValidationEvent()
        object Success : ValidationEvent()
    }
}