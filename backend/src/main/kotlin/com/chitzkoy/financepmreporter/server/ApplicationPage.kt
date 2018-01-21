package com.chitzkoy.financepmreporter.server

import kotlinx.html.*
import org.jetbrains.ktor.html.*

class ApplicationPage : Template<HTML> {
    val caption = Placeholder<TITLE>()
    val head = Placeholder<HEAD>()

    fun MetaDataContent.placeScript(src: String, integrity: String) {
        script {
            attributes["src"] = src
            attributes["integrity"] = integrity
            attributes["crossorigin"] = "anonymous"
        }
    }

    fun MetaDataContent.placeStylesheet(src: String, integrity: String? = null) {
        link {
            rel="stylesheet"
            attributes["href"] = src
            if (integrity != null) {
                attributes["integrity"] = integrity
            }
            attributes["crossorigin"] = "anonymous"
        }
    }


    override fun HTML.apply() {
        head {
            meta { charset = "utf-8" }
            meta {
                name = "viewport"
                content = "width=device-width, initial-scale=1.0"
            }
            meta {
                httpEquiv = "Content-Type"
                content = "text/html"
            }
            title {
                insert(caption)
            }
            insert(head)
            placeScript("https://code.jquery.com/jquery-3.2.1.min.js", "sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=")
            placeStylesheet("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css", "sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u")
            placeScript("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js", "sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa")
            placeStylesheet("https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css")
        }
        body {
            div { id = "root" }
            script(src = "/js/main.bundle.js")
        }
    }
}
