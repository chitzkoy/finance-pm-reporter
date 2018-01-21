package com.chitzkoy.financepmreporter.model.to

class TransactionTO(
        val id: Int,
        val name: String,
        val type: TransactionTypeTO,
        var category: CategoryTO?,
        val date: Long,
        val sum: Double,
        val account: AccountTO,
        val description: String,
        val source: SourceTO?
) {

    val isIncome = fun() : Boolean {
        return type.isIncome && category != null
    }

    override fun toString(): String {
        return "TransactionTO(name='$name', type=$type, category=$category, date=$date, sum=$sum, account=$account, description='$description', source=$source, isIncome=$isIncome)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as TransactionTO

        if (id != other.id) return false
        if (name != other.name) return false
        if (type != other.type) return false
        if (category != other.category) return false
        if (date != other.date) return false
        if (sum != other.sum) return false
        if (account != other.account) return false
        if (description != other.description) return false
        if (source != other.source) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + (category?.hashCode() ?: 0)
        result = 31 * result + date.hashCode()
        result = 31 * result + sum.hashCode()
        result = 31 * result + account.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + (source?.hashCode() ?: 0)
        return result
    }


}