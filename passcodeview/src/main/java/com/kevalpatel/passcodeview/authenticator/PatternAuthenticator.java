
package com.kevalpatel.passcodeview.authenticator;

import androidx.annotation.NonNull;

import com.kevalpatel.passcodeview.patternCells.PatternPoint;

import java.util.ArrayList;

public interface PatternAuthenticator {

    boolean isValidPattern(@NonNull final ArrayList<PatternPoint> patternPoints);
}
