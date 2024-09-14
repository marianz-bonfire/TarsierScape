package com.tarsier.marianz.tarsierscape.utilities;

import java.util.HashMap;
import java.util.Map;

public class StringUtil {
    public static String removeNonAlphanumeric(String word) {
        return word.replaceAll("[^A-Za-z]", "").trim();
    }
    
    
    /**
     * Check if word from dictionary contains characters of the sesrch word
     *
     * @param searchWord
     * @param word
     * @return
     */
    public static boolean containsChars(String searchWord, String word) {
        Map<Character, Integer> hm = new HashMap<>();

        for (int i = 0; i < word.length(); i++) {
            Character key = word.charAt(i);
            int count = 0;

            if (hm.containsKey(key)) {
                count = hm.get(key);
            }
            hm.put(key, ++count);
        }

        for (int i = 0; i < searchWord.length(); i++) {
            Character key = searchWord.charAt(i);

            if (hm.containsKey(key)) {
                int count = hm.get(key);

                if (count > 0) {
                    hm.put(key, --count);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Check the results of possible words if all consonants
     *
     * @param word
     * @return
     */
    public static boolean allConsonants(String word) {
        char[] vowels = {'a', 'e', 'i', 'o', 'u'};
        for (int i = 0; i < word.length(); i++) {
            Character key = word.charAt(i);
            for (int c = 0; c < vowels.length; c++) {
                if (key == vowels[c]) {
                    return false;
                }
            }
        }
        return true;
    }
}
