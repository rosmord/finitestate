package org.qenherkhopeshef.finitestate.lazy;

import static org.junit.Assert.*;

import org.junit.Test;
import static org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory.complement;
import static org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory.exact;
import static org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory.inter;
import static org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory.seq;
import static org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory.skip;

import static org.qenherkhopeshef.finitestate.lazy.SampleAutomata.*;

public class ComplementLanguageTest {

    @Test
    public void testNot__ab_STAR_ac_ENDNOT() {
        assertFalse(NOT_AB_STAR_AC_ENDNOT.recognize(CharHelper.fromString("ac")));
        assertFalse(NOT_AB_STAR_AC_ENDNOT.recognize(CharHelper.fromString("abac")));
        assertFalse(NOT_AB_STAR_AC_ENDNOT.recognize(CharHelper.fromString("ababac")));
        assertFalse(NOT_AB_STAR_AC_ENDNOT.recognize(CharHelper.fromString("abababac")));
        assertTrue(NOT_AB_STAR_AC_ENDNOT.recognize(CharHelper.fromString("aac")));
        assertTrue(NOT_AB_STAR_AC_ENDNOT.recognize(CharHelper.fromString("ABC")));
    }

    @Test
    public void testNot__aEmptyab_ENDNOT() {
        assertFalse(NOT_A_EMPTYAB_ENDNOT.recognize(CharHelper.fromString("aab")));
        assertTrue(NOT_A_EMPTYAB_ENDNOT.recognize(CharHelper.fromString("")));
        assertTrue(NOT_A_EMPTYAB_ENDNOT.recognize(CharHelper.fromString("ab")));
        assertTrue(NOT_A_EMPTYAB_ENDNOT.recognize(CharHelper.fromString("aabc")));

    }

    @Test
    public void testNot_empty() {
        assertFalse(NOT_EMPTY.recognize(CharHelper.fromString("")));
        assertTrue(NOT_EMPTY.recognize(CharHelper.fromString("A")));
        assertTrue(NOT_EMPTY.recognize(CharHelper.fromString("e")));
    }

    @Test
    public void testNot_one() {
        assertFalse(NOT_ONE.recognize(CharHelper.fromString("A")));
        assertTrue(NOT_ONE.recognize(CharHelper.fromString("")));
        assertTrue(NOT_ONE.recognize(CharHelper.fromString("ab")));
    }

    @Test
    public void testNot_two() {
        assertFalse(NOT_TWO.recognize(CharHelper.fromString("ac")));
        assertTrue(NOT_TWO.recognize(CharHelper.fromString("")));
        assertTrue(NOT_TWO.recognize(CharHelper.fromString("ABC")));
    }

    @Test
    public void testZero() {
        assertTrue(DOTPLUS_COMPLEMENT.recognize(CharHelper.fromString("")));
        assertFalse(DOTPLUS_COMPLEMENT.recognize(CharHelper.fromString("A")));
        assertFalse(DOTPLUS_COMPLEMENT.recognize(CharHelper.fromString("ab")));
    }

    @Test
    public void testAStarBMinusCD() {
        // Build the recogniser.
        RegularLanguageIF<Character> a_star_b;
        RegularLanguageIF<Character> a_star_b_minus_cd;
        RegularLanguageIF<Character> cd;
        a_star_b = seq(
                exact('a'),
                skip(),
                exact('b')
        );
        cd = seq(skip(), exact('c'), exact('d'), skip());
        a_star_b_minus_cd = inter(a_star_b, complement(cd));
        assertTrue(a_star_b_minus_cd.recognize(CharHelper.fromString("addfb")));
        assertTrue(a_star_b_minus_cd.recognize(CharHelper.fromString("addfczezehghdgfb")));
        assertFalse(a_star_b_minus_cd.recognize(CharHelper.fromString("adfdsfdscddfb")));
        assertFalse(a_star_b_minus_cd.recognize(CharHelper.fromString("adfdsf")));
    }
}
