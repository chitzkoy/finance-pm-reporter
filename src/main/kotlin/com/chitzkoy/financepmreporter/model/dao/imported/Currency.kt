package com.chitzkoy.financepmreporter.model.dao.imported

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import java.io.Serializable

/**
 * Created by dtikhonov on 07-Nov-17.
 */
class Currency(id: EntityID<Int>) : IntEntity(id), Serializable {
    companion object : IntEntityClass<Currency>(Currencies)

    var name by Currencies.name
    var shortName by Currencies.shortName

    fun toTO(): CurrencyTO {
        return CurrencyTO(name, shortName)
    }
}

object Currencies : IntIdTable("Currency") {
    val name = varchar("name", length = 50)
    val shortName = varchar("shortName", length = 5)
}

class CurrencyTO(
        val name: String,
        val shortName: String
) : Serializable {
    override fun toString(): String {
        return "CurrencyTO(shortName='$shortName')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

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