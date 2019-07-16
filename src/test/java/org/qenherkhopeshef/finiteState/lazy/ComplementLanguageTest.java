package org.qenherkhopeshef.finiteState.lazy;

import static org.junit.Assert.*;

import org.junit.Test;
import org.qenherkhopeshef.finiteState.lazy.character.StringToListHelper;

import static org.qenherkhopeshef.finiteState.lazy.SampleAutomata.*;

public class ComplementLanguageTest {

	@Test
	public void testNot__ab_STAR_ac_ENDNOT() {
		assertFalse(NOT_AB_STAR_AC_ENDNOT.recognize(StringToListHelper.fromString("ac")));
		assertFalse(NOT_AB_STAR_AC_ENDNOT.recognize(StringToListHelper.fromString("abac")));
		assertFalse(NOT_AB_STAR_AC_ENDNOT.recognize(StringToListHelper.fromString("ababac")));
		assertFalse(NOT_AB_STAR_AC_ENDNOT.recognize(StringToListHelper.fromString("abababac")));
		assertTrue(NOT_AB_STAR_AC_ENDNOT.recognize(StringToListHelper.fromString("aac")));
		assertTrue(NOT_AB_STAR_AC_ENDNOT.recognize(StringToListHelper.fromString("ABC")));
	}

	@Test
	public void testNot__aEmptyab_ENDNOT() {
		assertFalse(NOT_A_EMPTYAB_ENDNOT.recognize(StringToListHelper.fromString("aab")));
		assertTrue(NOT_A_EMPTYAB_ENDNOT.recognize(StringToListHelper.fromString("")));
		assertTrue(NOT_A_EMPTYAB_ENDNOT.recognize(StringToListHelper.fromString("ab")));
		assertTrue(NOT_A_EMPTYAB_ENDNOT.recognize(StringToListHelper.fromString("aabc")));

	}

	@Test
	public void testNot_empty() {
		assertFalse(NOT_EMPTY.recognize(StringToListHelper.fromString("")));
		assertTrue(NOT_EMPTY.recognize(StringToListHelper.fromString("A")));
		assertTrue(NOT_EMPTY.recognize(StringToListHelper.fromString("e")));
	}

	@Test
	public void testNot_one() {
		assertFalse(NOT_ONE.recognize(StringToListHelper.fromString("A")));
		assertTrue(NOT_ONE.recognize(StringToListHelper.fromString("")));
		assertTrue(NOT_ONE.recognize(StringToListHelper.fromString("ab")));
	}

	@Test
	public void testNot_two() {
		assertFalse(NOT_TWO.recognize(StringToListHelper.fromString("ac")));
		assertTrue(NOT_TWO.recognize(StringToListHelper.fromString("")));
		assertTrue(NOT_TWO.recognize(StringToListHelper.fromString("ABC")));
	}

	@Test
	public void testZero() {
		assertTrue(DOTPLUS_COMPLEMENT.recognize(StringToListHelper.fromString("")));
		assertFalse(DOTPLUS_COMPLEMENT.recognize(StringToListHelper.fromString("A")));
		assertFalse(DOTPLUS_COMPLEMENT.recognize(StringToListHelper.fromString("ab")));
	}

}
