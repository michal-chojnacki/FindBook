package com.github.michalchojnacki.findbook.domain

import javax.inject.Inject

class OcrCaptureValidateUseCase @Inject constructor() {
    operator fun invoke(query: String): Boolean {
        return query.length > 3
    }
}