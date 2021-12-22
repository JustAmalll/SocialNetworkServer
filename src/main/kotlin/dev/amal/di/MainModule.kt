package dev.amal.di

import com.google.gson.Gson
import dev.amal.data.repository.activity.ActivityRepository
import dev.amal.data.repository.activity.ActivityRepositoryImpl
import dev.amal.data.repository.chat.ChatRepository
import dev.amal.data.repository.chat.ChatRepositoryImpl
import dev.amal.data.repository.comment.CommentRepository
import dev.amal.data.repository.comment.CommentRepositoryImpl
import dev.amal.data.repository.follow.FollowRepository
import dev.amal.data.repository.follow.FollowRepositoryImpl
import dev.amal.data.repository.likes.LikeRepository
import dev.amal.data.repository.likes.LikeRepositoryImpl
import dev.amal.data.repository.post.PostRepository
import dev.amal.data.repository.post.PostRepositoryImpl
import dev.amal.data.repository.skill.SkillRepository
import dev.amal.data.repository.skill.SkillRepositoryImpl
import dev.amal.data.repository.user.UserRepository
import dev.amal.data.repository.user.UserRepositoryImpl
import dev.amal.service.*
import dev.amal.service.chat.ChatService
import dev.amal.util.Constants
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase(Constants.DATABASE_NAME)
    }
    single<UserRepository> {
        UserRepositoryImpl(get())
    }
    single<FollowRepository> {
        FollowRepositoryImpl(get())
    }
    single<PostRepository> {
        PostRepositoryImpl(get())
    }
    single<LikeRepository> {
        LikeRepositoryImpl(get())
    }
    single<CommentRepository> {
        CommentRepositoryImpl(get())
    }
    single<ActivityRepository> {
        ActivityRepositoryImpl(get())
    }
    single<SkillRepository> {
        SkillRepositoryImpl(get())
    }
    single<ChatRepository> {
        ChatRepositoryImpl(get())
    }
    single { UserService(get(), get()) }
    single { FollowService(get()) }
    single { PostService(get()) }
    single { LikeService(get(), get(), get()) }
    single { CommentService(get(), get()) }
    single { ActivityService(get(), get(), get()) }
    single { SkillService(get()) }
//    single { ChatService(get()) }
//    single { ChatController(get()) }

    single { Gson() }
}