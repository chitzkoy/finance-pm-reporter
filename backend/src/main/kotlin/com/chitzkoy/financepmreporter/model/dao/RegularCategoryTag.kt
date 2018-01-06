package com.chitzkoy.financepmreporter.model.dao

import com.chitzkoy.financepmreporter.model.dao.imported.*
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import java.io.Serializable

/**
 * Created by dtikhonov on 22-Nov-17.
 */
class RegularCategory(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RegularCategory>(RegularCategories)

    var category by Category referencedOn RegularCategories.category
    var currency by Currency referencedOn RegularCategories.currency

    fun toTO(): RegularCategoryTO {
        return RegularCategoryTO(category.toTO(), currency.toTO())
    }
}

object RegularCategories : IntIdTable("RegularCategories") {
    val category = reference("categoryId", Categories)
    val currency = reference("currencyId", Currencies)
}


class RegularCategoryTO(
        val category: CategoryTO,
        val currency: CurrencyTO
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RegularCategoryTO

        if (category != other.category) return false
        if (currency != other.currency) return false

        return true
    }

    override fun hashCode(): Int {
        var result = category.hashCode()
        result = 31 * result + currency.hashCode()
        return result
    }

    override fun toString(): String {
        return "RegularCategoryTO(category=$category, currency=$currency)"
    }


}