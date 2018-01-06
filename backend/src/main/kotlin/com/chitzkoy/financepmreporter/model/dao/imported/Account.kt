package com.chitzkoy.financepmreporter.model.dao.imported

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import java.io.Serializable

/**
 * Created by dtikhonov on 07-Nov-17.
 */
class Account(id: EntityID<Int>) : IntEntity(id), Serializable {
    companion object : IntEntityClass<Account>(Accounts)

    var name by Accounts.name
    var balance by Accounts.balance
    var currency by Currency referencedOn Accounts.currency
    val active by Accounts.active

    fun toTO() : AccountTO {
        return AccountTO(id.value, name, balance.toDouble(), currency.toTO(), active)
    }
}

object Accounts : IntIdTable("Account") {
    val name = varchar("name", length = 50)
    val balance = decimal("balance", 10, 2)
    val currency = reference("currencyId", Currencies)
    val active = bool("active")
}

class AccountTO(
        val id: Int,
        val name: String,
        val balance: Double,
        val currency: CurrencyTO,
        val active : Boolean
) : Serializable {

    override fun toString(): String {
        return "AccountTO(name='$name', balance=$balance, currency=$currency, active=$active)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

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