package com.github.michalchojnacki.findbook.ui.camera;

import androidx.annotation.NonNull;

public interface OcrOnTextDetectedListener {
    void onTextDetected(@NonNull String text);
}
