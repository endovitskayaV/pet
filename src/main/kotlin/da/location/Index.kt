package da.location

import da.controller.DataController
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.contentType

fun Route.index(datasController: DataController) {
    contentType(ContentType.Application.Json) {
        get<Index> {
            call.respond(datasController.index())
        }

        post<Index> {
            val message = call.receive<da.model.Data>()
            call.respond(datasController.create(message))
        }
    }
}