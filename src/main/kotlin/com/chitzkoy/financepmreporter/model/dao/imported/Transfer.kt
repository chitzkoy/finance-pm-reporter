package com.chitzkoy.financepmreporter.model.dao.imported

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import java.io.Serializable

/**
 * Created by dtikhonov on 07-Nov-17.
 */
class Transfer(id: EntityID<Int>) : IntEntity(id), Serializable {
    companion object : IntEntityClass<Transfer>(Transfers)

    var name by Transfers.name
    var from by Transaction referencedOn Transactions.id
    var to by Transaction referencedOn Transactions.id

    fun toTO() : TransferTO {
        return TransferTO(name, from.toTO(), to.toTO())
    }
}

object Transfers : IntIdTable("Transfer") {
    val name = varchar("name", length = 50)
    val from = reference("transactionIdFrom", Transactions)
    val to = reference("transactionIdTo", Transactions)
}

class TransferTO(val name: String, val from: TransactionTO, val to: TransactionTO) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TransferTO

        if (from != other.from) return false
        if (to != other.to) return false

        return true
    }

    override fun hashCode(): Int {
        var result = from.hashCode()
        result = 31 * result + to.hashCode()
        return result
    }
}