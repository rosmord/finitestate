package org.qenherkhopeshef.finitestate.lazy.character;

import org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory;
import org.qenherkhopeshef.finitestate.lazy.RegularLanguageIF;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for building basically a sequence language based on Characters.
 *
 * <p>
 * Not that useful (need to build factories for sub-languages). Improve...</p>
 *
 * Deprecated, because RegularLanguageFactory does better.
 * I know it's a bit weird to keep it in the initial distribution of the project, but
 * I still need to move some meaningful tests to the use of plain old RegularLanguageFactory.
 * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
 *
 */
@Deprecated
public class CharacterLanguageFactory {

    private CharacterLanguageFactory() {}
    /**
     * Add a String to the sequence.
     *
     * @param s
     * @return
     */
    public static RegularLanguageIF<Character> c(String s) {
        List<RegularLanguageIF<Character>> list = new ArrayList<>();
        for (char ch : s.toCharArray()) {
            list.add(RegularLanguageFactory.exact(ch));
        }
        return RegularLanguageFactory.seq(list);
    }

    /**
     * Add a character to the sequence.
     *
     * @param ch a character
     * @return
     */
    public static RegularLanguageIF<Character> c(char ch) {
        return RegularLanguageFactory.exact(ch);
    }

    /**
     * Add a character range to the sequence
     * <p>
     * Pre: min &lt;= max.
     * @param min min char (included)
     * @param max max char (included)
     * @return
     */
    public static RegularLanguageIF<Character> c(char min, char max) {
        return RegularLanguageFactory.label(new CharacterRangeLabel(min, max));
    }

    /**
     * Add a character outside a range.
     * <p>
     * Pre: min &lt;= max.</p>
     *
     * @param min min char (included)
     * @param max max char (included)
     * @return
     */
    public static RegularLanguageIF<Character> nc(char min, char max) {
        return RegularLanguageFactory.label(new ExcludedRangeLabel(min, max));
    }

}
