package com.chitzkoy.financepmreporter.model.dao.imported

import com.chitzkoy.financepmreporter.model.to.TransactionTO
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import java.io.Serializable

/**
 * Created by dtikhonov on 07-Nov-17.
 */
class Transaction(id: EntityID<Int>) : IntEntity(id), Serializable {
    companion object : IntEntityClass<Transaction>(Transactions)

    var name by Transactions.name
    var type by TransactionType referencedOn Transactions.type
    var category by Category optionalReferencedOn Transactions.category
    var date by Transactions.date
    var sum by Transactions.sum
    var account by Account referencedOn Transactions.account
    var description by Transactions.description
    var sourceId by Source optionalReferencedOn Transactions.sourceId

    fun toTO(): TransactionTO {
        return TransactionTO(
                id.value, name, type.toTO(), category?.toTO(),
                date.millis, sum.toDouble(), account.toTO(), description, sourceId?.toTO()
        )
    }
}

object Transactions : IntIdTable("Transaction"), Serializable {
    val name = varchar("name", length = 50)
    val type = reference("type", TransactionTypes)
    val category = optReference("categoryId", Categories)
    val date = datetime("date")
    val sum = decimal("sum", 10, 2)
    val account = reference("accountId", Accounts)
    val description = varchar("description", length = 120)
    val sourceId = optReference("sourceId", Sources)
}

//class TransactionTO(
//        val id: Int,
//        val name: String,
//        val type: TransactionTypeTO,
//        var category: CategoryTO?,
//        val date: DateTime,
//        val sum: Double,
//        val account: AccountTO,
//        val description: String,
//        val source: SourceTO?
//) : Serializable {
//
//    val isIncome = fun() : Boolean {
//        return type.isIncome && category != null
//    }
//
//    override fun toString(): String {
//        return "TransactionTO(name='$name', type=$type, category=$category, date=$date, sum=$sum, account=$account, description='$description', source=$source, isIncome=$isIncome)"
//    }
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as TransactionTO
//
//        if (id != other.id) return false
//        if (name != other.name) return false
//        if (type != other.type) return false
//        if (category != other.category) return false
//        if (date != other.date) return false
//        if (sum != other.sum) return false
//        if (account != other.account) return false
//        if (description != other.description) return false
//        if (source != other.source) return false
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        var result = id
//        result = 31 * result + name.hashCode()
//        result = 31 * result + type.hashCode()
//        result = 31 * result + (category?.hashCode() ?: 0)
//        result = 31 * result + date.hashCode()
//        result = 31 * result + sum.hashCode()
//        result = 31 * result + account.hashCode()
//        result = 31 * result + description.hashCode()
//        result = 31 * result + (source?.hashCode() ?: 0)
//        return result
//    }
//
//
//}