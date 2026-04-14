package org.example.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Koder Base62 używany do generowania krótkich, przyjaznych URL kodów.
 * Alfabet: 0-9, A-Z, a-z (62 znaki).
 */
public final class Base62Encoder {

    private static final String ALPHABET =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final BigInteger BASE = BigInteger.valueOf(62);

    private Base62Encoder() {}

    /**
     * Koduje liczbę nieujemną do postaci base62.
     *
     * @param number liczba do zakodowania (>= 0)
     * @return ciąg znaków base62
     */
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

    /**
     * Generuje unikalny kod base62 dla podanego URL.
     * Do SHA-256 hasha URL dodawany jest znacznik czasu (nanosekundy),
     * co gwarantuje unikalność nawet dla tego samego URL.
     *
     * @param url       oryginalny URL
     * @param codeLength żądana długość kodu (obcinana od lewej)
     * @return kod base62 o podanej długości
     */
    public static String generateCode(String url, int codeLength) {
        String input = url + System.nanoTime();
        byte[] hash = sha256(input.getBytes(StandardCharsets.UTF_8));

        // Bierzemy pierwsze 8 bajtów hasha jako liczbę dodatnią
        byte[] trimmed = new byte[9]; // +1 bajt zerowy → gwarantuje znak dodatni
        System.arraycopy(hash, 0, trimmed, 1, 8);
        BigInteger number = new BigInteger(trimmed);

        String encoded = encode(number);

        // Dopełniamy zerami od lewej jeśli zakodowany ciąg jest krótszy niż żądany
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
