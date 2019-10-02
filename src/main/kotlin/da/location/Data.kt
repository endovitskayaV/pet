package da.location

import da.controller.DataController
import io.ktor.application.call
import io.ktor.locations.delete
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route

fun Route.data(datasController: DataController) {

    get<Data> {
        datasController.show(it.id)?.let { it1 ->
            call.respond(it1)
        }
    }

    post<Data> {
        val message = call.receive<da.model.Data>()
        call.respond(datasController.update(it.id, message))
    }

    delete<Data> {
        call.respond(datasController.delete(it.id))
    }

}