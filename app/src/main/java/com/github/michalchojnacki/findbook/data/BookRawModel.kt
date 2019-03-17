package com.github.michalchojnacki.findbook.data

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false)
class BookRawModel {
    @set:Element
    @get:Element
    var title: String? = null

    @set:Element
    @get:Element
    var author: Author? = null

    @set:Element(name = "image_url")
    @get:Element(name = "image_url")
    var imageUrl: String? = null

    @Root(strict = false)
    class Author {
        @set:Element
        @get:Element
        var name: String? = null
    }
}