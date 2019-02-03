package com.github.michalchojnacki.findbook.domain.model

data class Book(val title: String,
                val author: String,
                val imageUrl: String,
                val ratingsCount: Int,
                val textReviewsCount: Int,
                val originalPublicationYear: Int?,
                val averageRating: Double)