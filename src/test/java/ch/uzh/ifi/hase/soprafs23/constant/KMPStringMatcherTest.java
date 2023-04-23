package ch.uzh.ifi.hase.soprafs23.constant;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class KMPStringMatcherTest {
    @Test
    void countOccurences_validInput_2Occurences() {
        String text = "Hello World, hello, seriouslyhElLo";
        String keyword = "heLlO";

        assertEquals(3, KMPStringMatcher.INSTANCE.countOccurences(text, keyword));
    }

    @Test
    void countOccurences_validInput_0Occurences() {
        String text = "World, seriously!";
        String keyword = "heLlO";

        assertEquals(0, KMPStringMatcher.INSTANCE.countOccurences(text, keyword));
    }
}
