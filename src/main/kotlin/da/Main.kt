package da

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import da.controller.DatasController
import da.model.Data
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import java.text.DateFormat

fun initDB() {
    val config = HikariConfig("/hikari.properties")
    config.addDataSourceProperty("characterEncoding","utf8"); //tried with UTF-8 as well
    config.addDataSourceProperty("useUnicode","true");
    val ds = HikariDataSource(config)
    Database.connect(ds)
}

fun Application.main() {
    install(Compression)
    install(CORS) {
        anyHost()
    }
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
    initDB()
    install(Routing) {

        val logger = KotlinLogging.logger { }

        route("/api") {
            route("/messages") {

                val datasController = DatasController()

                get("/") {
                    call.respond(datasController.index())
                }

                post("/") {
                    val message = call.receive<Data>()
                    logger.debug { message }
                    call.respond(datasController.create(message))
                }

                get("/{id}") {
                    val id = call.parameters["id"]!!.toInt()
                    datasController.show(id)?.let {
                            it1 -> call.respond(it1)
                    }
                }

                put("/{id}") {
                    val id = call.parameters["id"]!!.toInt()
                    val message = call.receive<Data>()
                    call.respond(datasController.update(id, message))
                }

                delete("/{id}") {
                    val id = call.parameters["id"]!!.toInt()
                    call.respond(datasController.delete(id))
                }
            }
        }
    }
}