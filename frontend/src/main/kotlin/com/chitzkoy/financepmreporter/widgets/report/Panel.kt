package com.chitzkoy.financepmreporter.widgets.report

import com.chitzkoy.financepmreporter.model.YearReportModel
import kotlinx.html.*
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*

class Panel(props: Props) : RComponent<Panel.Props, Panel.State>(props) {

    override fun RBuilder.render() {
        if (props.months.isEmpty()) {
            renderEmptyPanel()
        } else {
            renderPanel()
        }
    }

    private fun RBuilder.renderEmptyPanel() {
        div("report panel panel-warning") {
            div("panel-heading") {
                h3("panel-title") {
                    +"Не найдено"
                }
            }
            div("panel-body") {
                +"Отсуствует информация по заданному периоду"
            }
        }
    }

    private fun RBuilder.renderPanel() {
        val paginatorProps = TablePaginator.Props(props.year, props.currency, props.availableYears)
        val currencySelectorProps = CurrencySelector.Props(props.year, props.currency, props.availableCurrencies)

        div("report panel panel-primary") {
            div("panel-heading") {
                TablePaginator(paginatorProps)
                CurrencySelector(currencySelectorProps)
            }
            div("panel-body") {
                table("table table-condensed table-hover table-bordered table-responsive") {
                    colGroup {
                        col {
                            attrs["width"] = "200px"
                        }
                        col {
                            attrs.span = "${props.months.size}"
                            attrs["width"] = "80px"
                        }
                        col {
                            attrs.span = "${props.months.size}"
                            attrs["width"] = "100px"
                        }
                    }
                    thead {
                        tr {
                            th(ThScope.col) {
                                button {
                                    attrs.type = ButtonType.button
                                    attrs.title = "Fold all categories"
                                    attrs.classes = setOf("toggler", "btn", "btn-primary")
                                    attrs.onClick = "toggleTopLevelCategories(this)"

                                    i("fa fa-minus-square-o") {
                                        attrs["aria-hidden"] = "true"
                                    }
                                }

                                +"Category"
                            }

                            props.months.forEach {
                                th(ThScope.col, "text-center ${if (it == props.currentMonth) "current-month" else ""}") {
                                    +"$it"
                                }
                            }


                            th(ThScope.col, "text-center") {
                                +"AVG"
                                sub { +"${props.avgMonths} месяцев" }
                            }
                        }
                    }
                    tbody {
                        tr("summary info income") {
                            td {
                                +"Month income"
                            }

                            props.months.forEach {
                                val style = if (it == props.currentMonth) "current-month" else ""
                                td(style) {
                                    span("pull-right") {
                                        +"${props.yearTotals.totalsPerMonth[it]?.income}"
                                    }
                                }
                            }

                            td {
                                span("pull-right") {
                                    +"${props.yearTotals.avgTotals.income}"
                                }
                            }

                        }

                        tr("summary info text-danger") {
                            td {
                                +"Month expenses"
                            }

                            props.months.forEach {
                                val style = if (it == props.currentMonth) "current-month" else ""
                                td(style) {
                                    span("pull-right") {
                                        +"${props.yearTotals.totalsPerMonth[it]?.expenses}"
                                    }
                                }
                            }

                            td {
                                span("pull-right") {
                                    +"${props.yearTotals.avgTotals.expenses}"
                                }
                            }

                        }

                        tr("summary info balance") {
                            td {
                                +"Balance on the end of the month"
                            }

                            props.months.forEach {
                                val style = if (it == props.currentMonth) "current-month" else ""
                                td(style) {
                                    span("pull-right") {
                                        +"${props.yearTotals.totalsPerMonth[it]?.balance}"
                                    }
                                }
                            }

                            td {
                                span("pull-right") {
                                    +"${props.yearTotals.avgTotals.balance}"
                                }
                            }

                        }


//                        todo categoryInfoList

                    }
                }
            }
        }
    }

    class State() : RState
    class Props(model: YearReportModel) : RProps {
        val currency = model.currencyRepo.current
        val availableCurrencies = model.currencyRepo.available
        val year = model.yearRepo.current
        val availableYears = model.yearRepo.available
        var currentMonth = model.monthRepo.current
        var months = model.monthRepo.available
        var avgMonths = model.defaultAvgMonthDistance
        var yearTotals = model.yearTotals
    }
}