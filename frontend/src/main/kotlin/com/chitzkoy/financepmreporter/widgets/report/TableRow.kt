package com.chitzkoy.financepmreporter.widgets.report

import kotlinx.html.classes
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*

class TableRow(props: Props) : RComponent<TableRow.Props, TableRow.State>(props) {

    override fun RBuilder.render() {
        nav {
            ul("pagination") {
                li {
                    val previousYear = props.year - 1
                    val previousYearAvailable = props.availableYears.contains(previousYear)
                    attrs.classes = if (previousYearAvailable) setOf() else setOf("disabled")
                    a {
                        attrs.href = if (previousYearAvailable) "/report/${props.currency}/$previousYear" else "#"
                        attrs["aria-label"] = "Previous"

                        span {
                            attrs["aria-hidden"] = "true"
                            + "&laquo;"
                        }
                    }
                }

                li {
                    val nextYear = props.year + 1
                    val nextYearAvailable = props.availableYears.contains(nextYear)
                    attrs.classes = if (nextYearAvailable) setOf() else setOf("disabled")
                    a {
                        attrs.href = if (nextYearAvailable) "/report/${props.currency}/$nextYear" else "#"
                        attrs["aria-label"] = "Next"

                        span {
                            attrs["aria-hidden"] = "true"
                            + "&raquo;"
                        }
                    }

                }
            }
        }
    }

    class State : RState
    class Props(var year: Int, var currency: String, var availableYears: List<Int>) : RProps

}