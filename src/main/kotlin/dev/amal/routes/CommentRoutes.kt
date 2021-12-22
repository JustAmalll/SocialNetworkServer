package dev.amal.routes

import dev.amal.data.requests.CreateCommentRequest
import dev.amal.data.requests.DeleteCommentRequest
import dev.amal.data.responses.BasicApiResponse
import dev.amal.service.ActivityService
import dev.amal.service.CommentService
import dev.amal.service.LikeService
import dev.amal.util.ApiResponseMessages
import dev.amal.util.QueryParams
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createComment(
    commentService: CommentService,
    activityService: ActivityService
) {
    authenticate {
        post("/api/comment/create") {
            val request = call.receiveOrNull<CreateCommentRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val userId = call.userId
            when (commentService.createComment(request, userId)) {
                is CommentService.ValidationEvent.ErrorFieldEmpty -> {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = false,
                            message = ApiResponseMessages.FIELDS_BLANK
                        )
                    )
                }
                is CommentService.ValidationEvent.ErrorCommentTooLong -> {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = false,
                            message = ApiResponseMessages.COMMENT_TOO_LONG
                        )
                    )
                }
                is CommentService.ValidationEvent.UserNotFound -> {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = false,
                            message = ApiResponseMessages.USER_NOT_FOUND
                        )
                    )
                }
                is CommentService.ValidationEvent.Success -> {
                    activityService.addCommentActivity(
                        byUserId = userId,
                        postId = request.postId,
                    )
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(successful = true)
                    )
                }
            }
        }
    }
}

fun Route.getCommentsForPost(
    commentService: CommentService,
) {
    get("/api/comment/get") {
        val postId = call.parameters[QueryParams.PARAM_POST_ID] ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val comments = commentService.getCommentsForPost(postId, call.userId)
        call.respond(HttpStatusCode.OK, comments)
    }
}

fun Route.deleteComment(
    commentService: CommentService,
    likeService: LikeService
) {
    authenticate {
        delete("/api/comment/delete") {
            val request = call.receiveOrNull<DeleteCommentRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val comment = commentService.getCommentById(request.commentId)
            if (comment?.userId != call.userId) {
                call.respond(HttpStatusCode.Unauthorized)
                return@delete
            }
            val deleted = commentService.deleteComment(request.commentId)
            if (deleted) {
                likeService.deleteLikesForParent(request.commentId)
                call.respond(HttpStatusCode.OK, BasicApiResponse<Unit>(successful = true))
            } else call.respond(HttpStatusCode.NotFound, BasicApiResponse<Unit>(successful = false))
        }
    }
}