
package com.kevalpatel.passcodeview.authenticator;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import java.util.ArrayList;

public interface PinAuthenticator {

    @WorkerThread
    PinAuthenticationState isValidPin(@NonNull final ArrayList<Integer> pinDigits);

    enum PinAuthenticationState {
        SUCCESS,
        FAIL,
        NEED_MORE_DIGIT
    }
}
