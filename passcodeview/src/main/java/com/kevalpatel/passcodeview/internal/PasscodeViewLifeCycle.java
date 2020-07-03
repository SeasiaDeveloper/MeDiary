
package com.kevalpatel.passcodeview.internal;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.annotation.NonNull;


public interface PasscodeViewLifeCycle {

    void init();

    void setDefaults();

    void preparePaint();

    void parseTypeArr(@NonNull AttributeSet typedArray);

    void drawView(@NonNull Canvas canvas);

    void measureView(@NonNull Rect rootViewBounds);

    void onAuthenticationFail();

    void onAuthenticationSuccess();

    void reset();
}
