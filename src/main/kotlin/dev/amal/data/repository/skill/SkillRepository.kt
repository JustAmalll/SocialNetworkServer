package dev.amal.data.repository.skill

import dev.amal.data.models.Skill

interface SkillRepository {
    suspend fun getSkills(): List<Skill>
}