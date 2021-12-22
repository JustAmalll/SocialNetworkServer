package dev.amal.routes

import dev.amal.service.ActivityService
import dev.amal.util.Constants
import dev.amal.util.QueryParams
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getActivities(
    activityService: ActivityService,
) {
    authenticate {
        get("/api/activity/get") {
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull()
                ?: Constants.DEFAULT_PAGE_SIZE
            val activities = activityService.getActivitiesForUser(call.userId, page, pageSize)
            call.respond(HttpStatusCode.OK, activities)
        }
    }
}