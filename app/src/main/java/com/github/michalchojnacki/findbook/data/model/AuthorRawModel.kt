package com.github.michalchojnacki.findbook.data.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false)
class AuthorRawModel {
    @set:Element(name = "name")
    @get:Element(name = "name")
    var name: String? = null
}