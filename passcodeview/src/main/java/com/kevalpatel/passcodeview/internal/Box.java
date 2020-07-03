
package com.kevalpatel.passcodeview.internal;

import android.content.Context;

import androidx.annotation.NonNull;

abstract class Box implements PasscodeViewLifeCycle {

    /**
     * Reference to the {@link BasePasscodeView}.
     */
    private final BasePasscodeView mView;

    /**
     * Public constructor.
     *
     * @param rootView {@link BasePasscodeView} that contains this box.
     */
    Box(@NonNull final BasePasscodeView rootView) {
        mView = rootView;
    }

    /**
     * @return The root {@link BasePasscodeView} that contains this box.
     */
    @NonNull
    protected final BasePasscodeView getRootView() {
        return mView;
    }

    @NonNull
    protected final Context getContext() {
        return mView.getContext();
    }
}
