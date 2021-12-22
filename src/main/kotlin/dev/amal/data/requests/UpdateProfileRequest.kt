package dev.amal.data.requests

import dev.amal.data.responses.SkillDto

data class UpdateProfileRequest(
    val username: String,
    val bio: String,
    val gitHubUrl: String,
    val instagramUrl: String,
    val linkedInUrl: String,
    val skills: List<SkillDto>,
)
