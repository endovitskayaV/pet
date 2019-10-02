package da.entity

import org.jetbrains.exposed.sql.Table

object DataEntity: Table("data"){
    val id = integer("id").primaryKey().autoIncrement()
    val text = text("text")
}