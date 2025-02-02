package io.github.droideco.unseal;

import android.os.Build;

public final class Unseal {

    private Unseal() {
        throw new AssertionError("No io.github.droideco.unseal.Unseal instances for you!");
    }

    private static native void setHiddenAPIExemptions(String[] signaturePrefixes);

    private static final ThreadLocal<String[]> BUFFER;
    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            System.loadLibrary("unseal");
            BUFFER = new ThreadLocal<>() {
                @Override
                protected String[] initialValue() {
                    return new String[1];
                }
            };
        }
        else BUFFER = null;
    }

    public static void unsealAll() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) return;
        String[] signaturePrefixes = BUFFER.get();
        try {
            signaturePrefixes[0] = "L";
            setHiddenAPIExemptions(signaturePrefixes);
        }
        finally {
            signaturePrefixes[0] = null;
        }
    }

    public static void unsealByPrefix(String signaturePrefix) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) return;
        String[] signaturePrefixes = BUFFER.get();
        try {
            signaturePrefixes[0] = signaturePrefix == null ? "L" : signaturePrefix;
            setHiddenAPIExemptions(signaturePrefixes);
        }
        finally {
            signaturePrefixes[0] = null;
        }
    }

    public static void unsealClass(String className) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) return;
        String[] signaturePrefixes = BUFFER.get();
        try {
            signaturePrefixes[0] = className == null ? "L" : "L" + className.replace('.', '/') + ';';
            setHiddenAPIExemptions(signaturePrefixes);
        }
        finally {
            signaturePrefixes[0] = null;
        }
    }

}
