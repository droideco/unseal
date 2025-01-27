package io.github.droideco.unseal;

public final class Unseal {

    private Unseal() {
        throw new AssertionError("No io.github.droideco.unseal.Unseal instances for you!");
    }

    static {
        System.loadLibrary("unseal");
    }
    private static native void setHiddenAPIExemptions(String[] signaturePrefixes);

    private static final ThreadLocal<String[]> BUFFER = new ThreadLocal<>() {
        @Override
        protected String[] initialValue() {
            return new String[1];
        }
    };

    public static void unsealAll() {
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
