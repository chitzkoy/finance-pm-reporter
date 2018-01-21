package com.chitzkoy.financepmreporter

import com.chitzkoy.financepmreporter.util.getYearReport
import com.chitzkoy.financepmreporter.util.launch
import com.chitzkoy.financepmreporter.widgets.report.Panel
import kotlinx.html.colorInput
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import react.RBuilder
import react.dom.a
import react.dom.div
import react.dom.jsStyle
import react.dom.render
import kotlin.browser.document
import kotlin.browser.window

fun main(args: Array<String>) {
    println("Hello JavaScript!")
    window.addEventListener("DOMContentLoaded", EventListener { _ -> startUp() }, false)
}

fun startUp() {
    val rootDiv = document.getElementById("root")
    render(rootDiv) {
        app()
    }
}

fun RBuilder.app() {
//
    div {
        // Three different ways to define style properties are listed below
        attrs.jsStyle = kotlinext.js.js {
            width = "100px"
        }

        attrs {
            jsStyle {
                colorInput {  }
                height = "100px"
            }
        }

        attrs.jsStyle.backgroundColor = "red"

        // Setting an attribute
        attrs.attributes["title"] = "My title"

        // Setting a custom attribute
        attrs["my-attribute"] = "my-value"
        attrs["class"] = "class"

        a(href = "javascript:void(0)") {
//            attrs.onClickFunction = {
//                launch {
//                    val model = getYearReport(2017, "RUB")
//                    val rootDiv = document.getElementById("root")
//                    render(rootDiv) {
//                        Panel(Panel.Props(model))
//                    }
//                }
//            }

            +"Year Report"
        }

        // Appending children from props
//        props.children()
    }
}
