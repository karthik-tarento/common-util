package org.igot.common.auth;


public class Base64Util {
    public static final int DEFAULT = 0;
    public static final int URL_SAFE = 8;

    public static byte[] decode(String str, int flags) {
        // Handle URL-safe Base64 (used in JWT tokens)
        // Replace URL-safe characters with standard Base64 characters
        String standardBase64 = str.replace('-', '+').replace('_', '/');
        // Add padding if missing
        int paddingNeeded = (4 - standardBase64.length() % 4) % 4;
        standardBase64 = standardBase64 + "=".repeat(paddingNeeded);
        return java.util.Base64.getDecoder().decode(standardBase64);
    }

    public static byte[] decode(byte[] input, int flags) {
        return decode(new String(input), flags);
    }

    public static String encodeToString(byte[] input, int flags) {
        return java.util.Base64.getEncoder().encodeToString(input);
    }
}
