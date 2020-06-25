package org.qenherkhopeshef.finitestate.lazy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility methods for regular language built on characters.
 * @author rosmord.
 */
public class CharHelper {
	private CharHelper() {/* No contructor, static methods only*/}

	/**
	 * Converts a String into a list of characters.
	 * <p>Useful to feed lists to a RegularExtractor.</p>
	 * @param s
	 * @return
	 */
	public static List<Character> fromString(String s) {
	    return s.chars().mapToObj(i -> Character.valueOf((char)i)).collect(Collectors.toList());
	}
	
	/**
	 * Build a char-based language which recognizes a specific string.
	 * @param s
	 * @return
	 */
	public static RegularLanguageIF<Character> stringRecognizer(String s) {
		List<RegularLanguageIF<Character>> l = new ArrayList<>();
		for (char c : s.toCharArray()) {
			l.add(RegularLanguageFactory.exact(c));
		}
		return RegularLanguageFactory.seq(l);
	}
}
