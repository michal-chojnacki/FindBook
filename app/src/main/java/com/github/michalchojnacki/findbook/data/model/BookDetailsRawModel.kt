package com.github.michalchojnacki.findbook.data.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "GoodreadsResponse", strict = false)
class BookDetailsRawModel {
    @set:Element(name = "book")
    @get:Element(name = "book")
    var book: Book? = null

    @Root(strict = false)
    class Book {
        @set:Element(name = "id")
        @get:Element(name = "id")
        var id: Long? = null

        @set:Element(name = "title")
        @get:Element(name = "title")
        var title: String? = null

        @set:Element(name = "description",  required = false)
        @get:Element(name = "description",  required = false)
        var description: String? = null

        @set:Element(name = "reviews_widget")
        @get:Element(name = "reviews_widget")
        var reviewsWidget: String? = null

        @set:ElementList(name = "authors")
        @get:ElementList(name = "authors")
        var authors: List<AuthorRawModel>? = null

        @set:Element(name = "image_url")
        @get:Element(name = "image_url")
        var imageUrl: String? = null

        @set:Element(name = "average_rating")
        @get:Element(name = "average_rating")
        var averageRating: Double? = null

        @set:Element(name = "work")
        @get:Element(name = "work")
        var work: Work? = null
    }

    @Root(strict = false)
    class Work {
        @set:Element(name = "ratings_count")
        @get:Element(name = "ratings_count")
        var ratingsCount: Int = 0

        @set:Element(name = "text_reviews_count")
        @get:Element(name = "text_reviews_count")
        var textReviewsCount: Int = 0

        @set:Element(name = "original_publication_year", required = false)
        @get:Element(name = "original_publication_year", required = false)
        var originalPublicationYear: Int? = null
    }
}
