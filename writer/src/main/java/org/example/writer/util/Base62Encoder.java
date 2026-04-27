package org.example.writer.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Base62Encoder {

    private static final String ALPHABET =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final BigInteger BASE = BigInteger.valueOf(62);

    private Base62Encoder() {}

    public static String encode(BigInteger number) {
        if (number.signum() < 0) {
            throw new IllegalArgumentException("Liczba musi być nieujemna");
        }
        if (number.equals(BigInteger.ZERO)) {
            return String.valueOf(ALPHABET.charAt(0));
        }

        StringBuilder sb = new StringBuilder();
        BigInteger remaining = number;
        while (remaining.compareTo(BigInteger.ZERO) > 0) {
            int index = remaining.mod(BASE).intValue();
            sb.append(ALPHABET.charAt(index));
            remaining = remaining.divide(BASE);
        }
        return sb.reverse().toString();
    }

    public static String generateCode(String url, int codeLength) {
        String input = url + System.nanoTime();
        byte[] hash = sha256(input.getBytes(StandardCharsets.UTF_8));

        byte[] trimmed = new byte[9];
        System.arraycopy(hash, 0, trimmed, 1, 8);
        BigInteger number = new BigInteger(trimmed);

        String encoded = encode(number);

        if (encoded.length() < codeLength) {
            encoded = "0".repeat(codeLength - encoded.length()) + encoded;
        }
        return encoded.substring(encoded.length() - codeLength);
    }

    private static byte[] sha256(byte[] input) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 niedostępny", e);
        }
    }
}
