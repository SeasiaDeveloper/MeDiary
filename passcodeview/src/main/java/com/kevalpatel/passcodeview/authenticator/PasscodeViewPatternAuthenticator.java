
package com.kevalpatel.passcodeview.authenticator;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.kevalpatel.passcodeview.patternCells.PatternPoint;

import java.util.ArrayList;

public final class PasscodeViewPatternAuthenticator implements PatternAuthenticator {

    @NonNull
    private final PatternPoint[] mCorrectPattern;

    public PasscodeViewPatternAuthenticator(@NonNull final PatternPoint[] correctPattern) {
        mCorrectPattern = correctPattern;
    }

    @WorkerThread
    @Override
    public boolean isValidPattern(@NonNull final ArrayList<PatternPoint> patternPoints) {
        //This calculations won't take much time.
        //We are not blocking the UI.
        for (int i = 0; i < mCorrectPattern.length; i++)
            if (!mCorrectPattern[i].equals(patternPoints.get(i))) return false;

        return mCorrectPattern.length == patternPoints.size();
    }
}
