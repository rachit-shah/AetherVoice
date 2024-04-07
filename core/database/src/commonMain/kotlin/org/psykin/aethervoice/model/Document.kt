package org.psykin.aethervoice.model

import com.fleeksoft.ksoup.nodes.Element

data class Document(
    var id: String,
    var title: String,
    var content: Element,
    var format: DocumentFormat,
    var createdAt: String,
    var updatedAt: String,
    var checked: Boolean = false
)
