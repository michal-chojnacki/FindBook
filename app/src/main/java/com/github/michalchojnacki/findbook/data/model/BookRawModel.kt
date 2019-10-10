package com.github.michalchojnacki.findbook.data.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false)
class BookRawModel {
    @set:Element(name = "id")
    @get:Element(name = "id")
    var id: Long? = null

    @set:Element(name = "title")
    @get:Element(name = "title")
    var title: String? = null

    @set:Element(name = "author")
    @get:Element(name = "author")
    var author: AuthorRawModel? = null

    @set:Element(name = "image_url")
    @get:Element(name = "image_url")
    var imageUrl: String? = null
}