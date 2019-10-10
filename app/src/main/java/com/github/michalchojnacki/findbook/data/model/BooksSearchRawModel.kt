package com.github.michalchojnacki.findbook.data.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "GoodreadsResponse", strict = false)
class BooksSearchRawModel {
    @set:Element(name = "search")
    @get:Element(name = "search")
    var search: Search? = null

    @Root(strict = false)
    class Search {
        @set:ElementList(name = "results")
        @get:ElementList(name = "results")
        var results: List<Work>? = null
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

        @set:Element(name = "average_rating")
        @get:Element(name = "average_rating")
        var averageRating: Double? = null

        @set:Element(name = "best_book")
        @get:Element(name = "best_book")
        var bestBook: BookRawModel? = null
    }
}
