package com.chitzkoy.financepmreporter.model.dao.imported

import com.chitzkoy.financepmreporter.model.to.TransactionTypeTO
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import java.io.Serializable

/**
 * Created by dtikhonov on 07-Nov-17.
 */
class TransactionType(id: EntityID<Int>) : IntEntity(id), Serializable {
    companion object : IntEntityClass<TransactionType>(TransactionTypes)
    var name by TransactionTypes.name

    fun toTO() : TransactionTypeTO {
        return TransactionTypeTO(id.value, name, (id.value == 1))
    }
}

object TransactionTypes : IntIdTable("TransactionType") {
    val name = varchar("name", length = 50)
}

//class TransactionTypeTO(val id : Int, val name: String, val isIncome: Boolean) : Serializable {
//
//    override fun toString(): String {
//        return "TransactionTypeTO(name='$name', isIncome=$isIncome)"
//    }
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as TransactionTypeTO
//
//        if (id != other.id) return false
//        if (name != other.name) return false
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        var result = id
//        result = 31 * result + name.hashCode()
//        return result
//    }
//}