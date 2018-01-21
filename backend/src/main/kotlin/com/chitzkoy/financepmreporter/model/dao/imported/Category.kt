package com.chitzkoy.financepmreporter.model.dao.imported

import com.chitzkoy.financepmreporter.model.to.CategoryTO
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import java.io.Serializable

/**
 * Created by dtikhonov on 07-Nov-17.
 */
class Category(id: EntityID<Int>) : IntEntity(id), Serializable {
    companion object : IntEntityClass<Category>(Categories)

    var name by Categories.name
    var type by TransactionType referencedOn Categories.type
    var parent by Category optionalReferencedOn Categories.parent
    var orderId by Categories.orderId

    fun toTO(): CategoryTO {
        return CategoryTO(id.value, name, type.toTO(), parent?.toTO(), orderId)
    }
}

object Categories : IntIdTable("Category") {
    val name = varchar("name", length = 50)
    val type = reference("type", TransactionTypes)
    val parent = reference("parentId", id).nullable()
    var orderId = integer("orderId")
}

//class CategoryTO(
//        val id: Int,
//        val name: String,
//        val type: TransactionTypeTO,
//        val parent: CategoryTO?,
//        val orderId: Int
//) : Serializable {
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as CategoryTO
//
//        if (id != other.id) return false
//        if (name != other.name) return false
//        if (type != other.type) return false
//        if (parent != other.parent) return false
//        if (orderId != other.orderId) return false
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        var result = id
//        result = 31 * result + name.hashCode()
//        result = 31 * result + type.hashCode()
//        result = 31 * result + (parent?.hashCode() ?: 0)
//        result = 31 * result + orderId
//        return result
//    }
//
//    override fun toString(): String {
//        return "CategoryTO(id=$id, name='$name', orderId=$orderId)"
//    }
//
//}