package ch.uzh.ifi.hase.soprafs23.constant;

public class KMPStringMatcher {
    public static KMPStringMatcher INSTANCE = new KMPStringMatcher();

    private KMPStringMatcher() {
    }

    private int[] substringSearchPattern(String keyword) {
        int[] pattern = new int[keyword.length()];
        int i = 1, j = 0;

        while (i < keyword.length()) {
            if (keyword.charAt(i) == keyword.charAt(j)) {
                pattern[i] = j + 1;
                i++;
                j++;
            } else if (j > 0) {
                j = pattern[j - 1];
            } else {
                pattern[i] = 0;
                i++;
            }
        }

        return pattern;
    }

    public int countOccurences(String text, String keyword) {
        if (text.length() < keyword.length()) {
            return 0;
        }
        int i = 0, j = 0, counter = 0;
        String lowerCaseText = text.toLowerCase(), lowerCaseKeyword = keyword.toLowerCase();
        int[] pattern = substringSearchPattern(lowerCaseKeyword);
        while (i < text.length()) {
            if (lowerCaseText.charAt(i) == lowerCaseKeyword.charAt(j)) {
                i++;
                j++;
            } else if (j > 0) {
                j = pattern[j - 1];
            } else {
                i++;
            }
            if (j == lowerCaseKeyword.length()) {
                j = 0;
                counter++;
            }
        }

        return counter;
    }
}
