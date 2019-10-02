package da.controller

import da.entity.DataEntity
import da.model.Data
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class DataController {

    fun index(): ArrayList<Data> {
        val messages: ArrayList<Data> = arrayListOf()
        transaction {
            DataEntity.selectAll().map { messages.add(Data(id = it[DataEntity.id], text = it[DataEntity.text])) }
        }
        return messages
    }

    fun create(message: Data): Data {
        val messageId = transaction {
            DataEntity.insert {
                it[DataEntity.text] = message.text
            }.generatedKey
        }
        return message.copy(id = messageId!!.toInt())
    }

    fun show(id: Int): Data? {
        return transaction {
            DataEntity.select { DataEntity.id eq id }
                .map { Data(id = it[DataEntity.id], text = it[DataEntity.text]) }
                .firstOrNull()
        }
    }

    fun update(id: Int, newData: Data): Data {
        transaction {
            DataEntity.update({ DataEntity.id eq id }) {
                it[text] = newData.text
            }
        }
        return Data(id = id, text = newData.text)
    }

    fun delete(id: Int) {
        transaction {
            DataEntity.deleteWhere { DataEntity.id eq id }
        }
    }

}