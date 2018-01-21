package com.chitzkoy.financepmreporter.model.to

class CategoryTO(
        val id: Int,
        val name: String,
        val type: TransactionTypeTO,
        val parent: CategoryTO?,
        val orderId: Int
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as CategoryTO

        if (id != other.id) return false
        if (name != other.name) return false
        if (type != other.type) return false
        if (parent != other.parent) return false
        if (orderId != other.orderId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + (parent?.hashCode() ?: 0)
        result = 31 * result + orderId
        return result
    }

    override fun toString(): String {
        return "CategoryTO(id=$id, name='$name', orderId=$orderId)"
    }

}