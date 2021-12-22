package dev.amal.data.repository.skill

import dev.amal.data.models.Skill
import org.litote.kmongo.coroutine.CoroutineDatabase

class SkillRepositoryImpl(
    private val db: CoroutineDatabase
) : SkillRepository {

    private val skills = db.getCollection<Skill>()

    override suspend fun getSkills(): List<Skill> = skills.find().toList()

}