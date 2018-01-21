package com.chitzkoy.financepmreporter.model.to

class TransferTO(val name: String, val from: TransactionTO, val to: TransactionTO) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

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