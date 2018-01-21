package com.chitzkoy.financepmreporter.model.to

class AccountTO(
        val id: Int,
        val name: String,
        val balance: Double,
        val currency: CurrencyTO,
        val active : Boolean
) {

    override fun toString(): String {
        return "AccountTO(name='$name', balance=$balance, currency=$currency, active=$active)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as AccountTO

        if (id != other.id) return false
        if (name != other.name) return false
        if (balance != other.balance) return false
        if (currency != other.currency) return false
        if (active != other.active) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + balance.hashCode()
        result = 31 * result + currency.hashCode()
        result = 31 * result + active.hashCode()
        return result
    }
}