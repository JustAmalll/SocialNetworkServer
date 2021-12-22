package dev.amal.data.requests

data class CreateCommentRequest(
    val comment: String,
    val postId: String,
)
