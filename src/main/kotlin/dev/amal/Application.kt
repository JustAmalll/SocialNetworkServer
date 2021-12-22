package dev.amal

import dev.amal.di.mainModule
import io.ktor.application.*
import dev.amal.plugins.*
import org.koin.ktor.ext.Koin

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    install(Koin) { modules(mainModule) }
    configureSecurity()
    configureSockets()
    configureRouting()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
}
