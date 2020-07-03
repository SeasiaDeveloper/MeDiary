
package com.kevalpatel.passcodeview.indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import androidx.annotation.Dimension;
import androidx.annotation.NonNull;

import com.kevalpatel.passcodeview.internal.BasePasscodeView;

public abstract class Indicator {
    @NonNull
    private final Rect mBound;

    @NonNull
    private final Indicator.Builder mBuilder;

    Indicator(@NonNull final Indicator.Builder builder,
              @NonNull final Rect bound) {
        mBound = bound;
        mBuilder = builder;
    }

    @NonNull
    public Rect getBound() {
        return mBound;
    }

    @NonNull
    protected final BasePasscodeView getRootView() {
        return mBuilder.getRootView();
    }

    @NonNull
    protected final Context getContext() {
        return mBuilder.getContext();
    }

    public abstract void draw(@NonNull final Canvas canvas, final boolean isFilled);

    public abstract void onAuthFailed();

    public abstract void onAuthSuccess();

    public static abstract class Builder {

        @NonNull
        private final BasePasscodeView mPasscodeView;

        public Builder(@NonNull final BasePasscodeView passcodeView) {
            mPasscodeView = passcodeView;
        }

        @NonNull
        protected final BasePasscodeView getRootView() {
            return mPasscodeView;
        }

        @Dimension
        public abstract float getIndicatorWidth();

        @NonNull
        protected final Context getContext() {
            return mPasscodeView.getContext();
        }

        public abstract Indicator buildInternal(@NonNull final Rect bound);
    }
}
