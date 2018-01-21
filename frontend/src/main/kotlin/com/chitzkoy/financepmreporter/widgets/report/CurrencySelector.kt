package com.chitzkoy.financepmreporter.widgets.report

import kotlinx.html.ButtonType
import kotlinx.html.classes
import kotlinx.html.id
import kotlinx.html.title
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*

class CurrencySelector(props: Props) : RComponent<CurrencySelector.Props, CurrencySelector.State>(props) {

    override fun RBuilder.render() {
        div("dropdown pull-right") {
            button {
                attrs.id = "currencyDropdown"
                attrs.type = ButtonType.button
                attrs.title = props.currency
                attrs.classes = setOf("btn", "btn-info", "dropdown-toggle")
                attrs["data-toggle"] = "dropdown"
                attrs["aria-haspopup"] = "true"
                attrs["aria-expanded"] = "true"

                span("caret") {}
            }

            ul("dropdown-menu") {
                attrs["aria-labelledby"] = "currencyDropdown"

                props.availableCurrencies.forEach {
                    val style = if (it == props.currency) "disabled" else ""
                    li(style) {
                        a("/report/$it/${props.year}") {
                            +it
                        }
                    }
                }
            }

        }
    }

    class State : RState
    class Props(val year: Int, val currency: String, val availableCurrencies: List<String>) : RProps

}