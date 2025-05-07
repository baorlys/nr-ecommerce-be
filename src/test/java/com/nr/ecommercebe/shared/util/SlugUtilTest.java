package com.nr.ecommercebe.shared.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SlugUtilTest {

    @Test
    void generateSlug_simpleString_returnsCorrectSlug() {
        // Arrange
        String input = "Hello World";

        // Act
        String result = SlugUtil.generateSlug(input);

        // Assert
        assertEquals("hello-world", result);
    }

    @Test
    void generateSlug_stringWithAccents_returnsSlugWithoutAccents() {
        // Arrange
        String input = "Café Müller";

        // Act
        String result = SlugUtil.generateSlug(input);

        // Assert
        assertEquals("cafe-muller", result);
    }

    @Test
    void generateSlug_stringWithSpecialCharacters_returnsSlugWithoutSpecialCharacters() {
        // Arrange
        String input = "Hello!@#World$%^";

        // Act
        String result = SlugUtil.generateSlug(input);

        // Assert
        assertEquals("helloworld", result);
    }

    @Test
    void generateSlug_stringWithMultipleSpacesAndHyphens_returnsSingleHyphenSlug() {
        // Arrange
        String input = "Hello   World--Test";

        // Act
        String result = SlugUtil.generateSlug(input);

        // Assert
        assertEquals("hello-world-test", result);
    }

    @Test
    void generateSlug_stringWithLeadingTrailingSpaces_returnsTrimmedSlug() {
        // Arrange
        String input = "  Hello World  ";

        // Act
        String result = SlugUtil.generateSlug(input);

        // Assert
        assertEquals("hello-world", result);
    }

    @Test
    void generateSlug_emptyString_returnsEmptyString() {
        // Arrange
        String input = "";

        // Act
        String result = SlugUtil.generateSlug(input);

        // Assert
        assertEquals("", result);
    }

    @Test
    void generateSlug_onlySpecialCharacters_returnsEmptyString() {
        // Arrange
        String input = "!@#$%^";

        // Act
        String result = SlugUtil.generateSlug(input);

        // Assert
        assertEquals("", result);
    }


    @Test
    void generateSlug_mixedComplexInput_returnsCorrectSlug() {
        // Arrange
        String input = "  Café & Müller!  Test--123  ";

        // Act
        String result = SlugUtil.generateSlug(input);

        // Assert
        assertEquals("cafe-muller-test-123", result);
    }
}