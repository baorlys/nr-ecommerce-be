package com.nr.ecommercebe.common.util;

import java.text.Normalizer;
import java.util.Locale;

public class SlugUtil {
    private SlugUtil() {
        // Private constructor to prevent instantiation
    }

    public static String generateSlug(String name) {
        String normalizedName = Normalizer.normalize(name, Normalizer.Form.NFD);
        String withoutAccents = normalizedName.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        return withoutAccents
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s-]", "") // Remove special characters
                .trim()
                .replaceAll("\\s+", "-") // Replace spaces with hyphens
                .replaceAll("-+", "-"); // Replace multiple hyphens with a single hyphen

    }
}
