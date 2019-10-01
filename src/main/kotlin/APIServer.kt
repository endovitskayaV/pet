@file:JvmName("APIServer")

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson

import io.ktor.http.ContentType
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main(args: Array<String>) {
    val toDoList = ArrayList<ToDo>();

    val jsonResponse = """{
    "id": 1,
    "task": "Pay waterbill",
    "description": "Pay water bill today",
}"""

    embeddedServer(Netty, 8080) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        install(DefaultHeaders) {
            header("X-Developer", "Baeldung")
        }

        routing() {
            route("/todo") {
                post {
                    var toDo = call.receive<ToDo>();
                    toDo.id = toDoList.size;
                    toDoList.add(toDo);
                    call.respond("Added")

                }
                delete("/{id}") {
                    call.respond(toDoList.removeAt(call.parameters["id"]!!.toInt()));
                }
                get("/{id}") {
                    call.respond(toDoList[call.parameters["id"]!!.toInt()]);
                }
                get {
                    call.respond(toDoList);
                }
            }
        }



//        install(Routing) {
//            get("/todo") {
//                call.respondText(jsonResponse, ContentType.Application.Json)
//            }
//            get("/author") {
//                val author = Author("baeldung", "baeldung.com")
//                call.respond(author)
//            }
//        }

    }.start(wait = true)//listen for connections
}