package com.chitzkoy.financepmreporter.model

import com.chitzkoy.financepmreporter.model.dao.imported.CategoryTO
import java.time.Month

data class CategoryInfo(
        val category: CategoryTO,
        val totalsPerMonth: MutableMap<Month, Double>,
        val childCategories: MutableList<CategoryInfo>,
        var avg: Double? = null
)