package dev.amal.service

import dev.amal.data.models.Skill
import dev.amal.data.repository.skill.SkillRepository

class SkillService(
    private val repository: SkillRepository
) {
    suspend fun getSkills(): List<Skill> = repository.getSkills()
}