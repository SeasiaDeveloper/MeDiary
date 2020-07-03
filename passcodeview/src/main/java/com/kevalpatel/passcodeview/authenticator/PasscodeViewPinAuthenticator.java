
package com.kevalpatel.passcodeview.authenticator;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import java.util.ArrayList;

public final class PasscodeViewPinAuthenticator implements PinAuthenticator {

    private final int[] mCorrectPin;

    public PasscodeViewPinAuthenticator(int[] correctPin) {
        mCorrectPin = correctPin;
    }

    @WorkerThread
    @Override
    public PinAuthenticationState isValidPin(@NonNull final ArrayList<Integer> pinDigits) {
        //Check if the size of the entered pin matches the correct pin
        if (!isValidPinLength(pinDigits.size())) return PinAuthenticationState.NEED_MORE_DIGIT;

        //This calculations won't take much time.
        //We are not blocking the UI.
        for (int i = 0; i < mCorrectPin.length; i++) {
            if (mCorrectPin[i] != pinDigits.get(i)) {

                //Digit did not matched
                //Wrong PIN
                return PinAuthenticationState.FAIL;
            }
        }

        //PIN is correct
        return PinAuthenticationState.SUCCESS;
    }

    private boolean isValidPinLength(final int typedLength) {
        return typedLength == mCorrectPin.length;
    }
}
