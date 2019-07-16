package org.qenherkhopeshef.finiteState.lazy.character;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Useful methods for character-based languages.
 */
public class StringToListHelper {
    /**
     * Converts a String into a list of characters.
     * <p>Useful to feed lists to a RegularExtractor.</p>
     * @param s
     * @return
     */
    public static List<Character> fromString(String s) {
        return s.chars().mapToObj(i -> Character.valueOf((char)i)).collect(Collectors.toList());
    }
    
}