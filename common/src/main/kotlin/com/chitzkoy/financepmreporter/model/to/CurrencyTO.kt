package com.chitzkoy.financepmreporter.model.to

class CurrencyTO(
        val name: String,
        val shortName: String
) {
    override fun toString(): String {
        return "CurrencyTO(shortName='$shortName')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as CurrencyTO

        if (name != other.name) return false
        if (shortName != other.shortName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + shortName.hashCode()
        return result
    }


}