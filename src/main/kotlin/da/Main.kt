package da

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import da.controller.DataController
import da.entity.DataEntity
import da.location.data
import da.location.index
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.locations.Locations
import io.ktor.routing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.text.DateFormat

fun initDB() {
    val config = HikariConfig("/hikari.properties")
    val ds = HikariDataSource(config)
    Database.connect(ds)
}

fun Application.main() {
    install(Locations)
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

    val datasController = DataController()

    routing {
        index(datasController)
        data(datasController)
    }

    transaction {

        SchemaUtils.create(DataEntity) //creates table
    }
}