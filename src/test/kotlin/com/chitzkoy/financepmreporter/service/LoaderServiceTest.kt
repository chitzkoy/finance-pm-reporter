package com.chitzkoy.financepmreporter.service

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

/**
 * Created by dtikhonov on 19-Nov-17.
 */

class LoaderServiceTest {

    @Test
    fun mergeCategoriesTest() {
        val parser = Parser()

        val fileStream1 = File("categories1.data").inputStream()
        val fileStream2 = File("categories2.data").inputStream()
        val categories1 = parser.parse(fileStream1) as JsonObject
        val categories2 = parser.parse(fileStream2) as JsonObject
        val actual = mergeCategories(categories1, categories2)
        actual.sortWith(categoryComparator)

        val expected = parser.parse("categoriesResult.data") as JsonArray<JsonObject>

        assertEquals(expected, actual)
    }
}