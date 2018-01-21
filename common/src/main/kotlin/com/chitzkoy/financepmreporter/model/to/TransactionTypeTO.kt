package com.chitzkoy.financepmreporter.model.to

class TransactionTypeTO(val id : Int, val name: String, val isIncome: Boolean) {

    override fun toString(): String {
        return "TransactionTypeTO(name='$name', isIncome=$isIncome)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as TransactionTypeTO

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        return result
    }
}