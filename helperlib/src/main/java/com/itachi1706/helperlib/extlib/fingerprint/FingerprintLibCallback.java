package com.itachi1706.helperlib.extlib.fingerprint;

/**
 * @deprecated Slated for removal soon
 */
@Deprecated
public interface FingerprintLibCallback {

    void onFingerprintLibReady(FingerprintLib fingerprintLib);

    void onFingerprintLibListening(boolean newFingerprint);

    void onFingerprintLibAuthenticated(FingerprintLib fingerprintLib);

    void onFingerprintLibError(FingerprintLib fingerprintLib, FingerprintLibErrorType type, Exception e);
}