package com.chitzkoy.financepmreporter

import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import react.RBuilder
import react.dom.div
import react.dom.jsStyle
import react.dom.render
import kotlin.browser.document
import kotlin.browser.window

fun main(args: Array<String>) {
    println("Hello JavaScript!")
    window.addEventListener("DOMContentLoaded", EventListener { event -> startUp(event) }, false)
}

fun startUp(event : Event) {
    val rootDiv = document.getElementById("root")
    render(rootDiv) {
        app()
    }
}

fun RBuilder.app() {
    div {
        // Three different ways to define style properties are listed below
        attrs.jsStyle = kotlinext.js.js {
            width = "100px"
        }

        attrs {
            jsStyle {
                height = "100px"
            }
        }

        attrs.jsStyle.backgroundColor = "red"

        // Setting an attribute
        attrs.attributes["title"] = "My title"

        // Setting a custom attribute
        attrs["my-attribute"] = "my-value"
        attrs["class"] = "class"

        // Appending children from props
//        props.children()
    }
}
