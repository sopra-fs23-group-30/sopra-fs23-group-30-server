package ch.uzh.ifi.hase.soprafs23.constant;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class KMPStringMatcherTest {
    @Test
    void countOccurences_validInput_2Occurences() {
        KMPStringMatcher matcher = new KMPStringMatcher();
        String text = "Hello World, hello, seriouslyhElLo";
        String keyword = "heLlO";

        assertEquals(3, matcher.countOccurences(text, keyword));
    }

    @Test
    void countOccurences_validInput_0Occurences() {
        KMPStringMatcher matcher = new KMPStringMatcher();
        String text = "World, seriously!";
        String keyword = "heLlO";

        assertEquals(0, matcher.countOccurences(text, keyword));
    }
}
