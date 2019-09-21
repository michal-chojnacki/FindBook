package com.github.michalchojnacki.findbook.ui.camera

import com.github.michalchojnacki.findbook.domain.OcrCaptureValidateUseCase
import com.github.michalchojnacki.findbook.ui.common.BaseViewModel
import javax.inject.Inject

class OcrCaptureViewModel @Inject constructor(private val ocrCaptureValidate: OcrCaptureValidateUseCase) :
    BaseViewModel() {

    fun validateText(query: String) = ocrCaptureValidate(query)
}