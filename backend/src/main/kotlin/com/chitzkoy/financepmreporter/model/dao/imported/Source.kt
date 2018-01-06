package com.chitzkoy.financepmreporter.model.dao.imported

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import java.io.Serializable

/**
 * Created by dtikhonov on 17-Nov-17.
 */
class Source(id: EntityID<Int>) : IntEntity(id), Serializable {
    companion object : IntEntityClass<Source>(Sources)

    var name by Sources.name

    fun toTO(): SourceTO {
        return SourceTO(id.value, name)
    }
}

object Sources : IntIdTable("Source") {
    val name = varchar("name", length = 50)
}

class SourceTO(val id: Int, val name: String) : Serializable {

    override fun toString(): String {
        return "SourceTO(name='$name')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SourceTO

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