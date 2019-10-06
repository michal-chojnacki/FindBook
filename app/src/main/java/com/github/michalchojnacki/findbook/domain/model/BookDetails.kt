package com.github.michalchojnacki.findbook.domain.model

data class BookDetails(
    val id: Long,
    val title: String,
    val authors: List<String>,
    val imageUrl: String,
    val ratingsCount: Int,
    val textReviewsCount: Int,
    val originalPublicationYear: Int?,
    val averageRating: Double,
    val reviewsWidget : String
)