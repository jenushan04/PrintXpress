package com.printxpress.app.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** SHA-256 password hashing. */
public class PasswordUtil {

    private static final String SALT = "PrintXpress_2026_Salt";

    public static String hash(String password) {
        if (password == null) return "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest((SALT + password).getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            // Fallback: should never happen on Android
            return password;
        }
    }

    public static boolean verify(String plain, String hashed) {
        return hash(plain).equals(hashed);
    }
}
