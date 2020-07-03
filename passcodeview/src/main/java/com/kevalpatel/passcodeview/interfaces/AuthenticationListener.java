

package com.kevalpatel.passcodeview.interfaces;

public interface AuthenticationListener {

    /**
     * This method indicates that authentication is successful (either by matching pin ,matching
     * password or correct fingerprint).
     */
    void onAuthenticationSuccessful();

    /**
     * Authentication failed.
     */
    void onAuthenticationFailed();
}
