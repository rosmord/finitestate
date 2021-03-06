package org.qenherkhopeshef.finitestate.lazy;

import org.qenherkhopeshef.finitestate.lazy.RegularLanguageIF;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.qenherkhopeshef.finitestate.lazy.SampleAutomata.*;

import static org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory.*;

public class SequenceLanguageTest {


	@Test
	public void testABC() {
		RegularLanguageIF<Character> abc = RegularLanguageFactory.exactSequence('a', 'b', 'c');
		assertTrue(abc.recognize(CharHelper.fromString("abc")));
		assertFalse(abc.recognize(CharHelper.fromString("a")));
		assertFalse(abc.recognize(CharHelper.fromString("c")));
		assertFalse(abc.recognize(CharHelper.fromString("abcde")));
	}

	@Test
	public void testAEmptybEmpty() {
		assertTrue(A_EMPTY_B_EMPTY.recognize(CharHelper.fromString("ab")));
		assertFalse(A_EMPTY_B_EMPTY.recognize(CharHelper.fromString("a")));
		assertFalse(A_EMPTY_B_EMPTY.recognize(CharHelper.fromString("c")));
		assertFalse(A_EMPTY_B_EMPTY.recognize(CharHelper.fromString("abcde")));
	}

	@Test
	public void testAemptyAB() {
		assertTrue(A_EMPTY_A_B.recognize(CharHelper.fromString("aab")));
		assertFalse(A_EMPTY_A_B.recognize(CharHelper.fromString("ab")));
		assertFalse(A_EMPTY_A_B.recognize(CharHelper.fromString("aaab")));
		assertFalse(A_EMPTY_A_B.recognize(CharHelper.fromString("abcde")));
	}

	@Test
	public void testAbStarAC() {
		assertTrue(AB_STAR_AC.recognize(CharHelper.fromString("ac")));
		assertTrue(AB_STAR_AC.recognize(CharHelper.fromString("abac")));
		assertTrue(AB_STAR_AC.recognize(CharHelper.fromString("ababac")));
		assertTrue(AB_STAR_AC.recognize(CharHelper.fromString("abababac")));
		assertFalse(AB_STAR_AC.recognize(CharHelper.fromString("aac")));
		assertFalse(AB_STAR_AC.recognize(CharHelper.fromString("abc")));
	}

	@Test
	public void testaa_THEN_aStar_bc_inter_a_bcStar_THEN_bb() {
		assertTrue(AA_THEN_A_STAR_BC_INTER_A_BC_STAR_THEN_CC.recognize(CharHelper.fromString("aaabccc")));
		assertFalse(AA_THEN_A_STAR_BC_INTER_A_BC_STAR_THEN_CC.recognize(CharHelper.fromString("ABC")));
		assertFalse(AA_THEN_A_STAR_BC_INTER_A_BC_STAR_THEN_CC.recognize(CharHelper.fromString("A")));
		assertFalse(AA_THEN_A_STAR_BC_INTER_A_BC_STAR_THEN_CC.recognize(CharHelper.fromString("abcde")));
	}

	@Test
	public void testnot__ab_STAR_ac_ENDNOT_abab() {
		// A bit more tricky...
		assertTrue(NOT_AB_STAR_AC_ENDNOT_ABAB.recognize(CharHelper.fromString("abab")));
		assertTrue(NOT_AB_STAR_AC_ENDNOT_ABAB.recognize(CharHelper.fromString("asdfdsfdsfzfhzefabab")));
		assertFalse(NOT_AB_STAR_AC_ENDNOT_ABAB.recognize(CharHelper.fromString("acabab")));
		assertFalse(NOT_AB_STAR_AC_ENDNOT_ABAB.recognize(CharHelper.fromString("abacabab")));
	}

	@Test
	public void testb_tag() {
		assertTrue(B_TAG_NO_BTAG.recognize(CharHelper.fromString("<b>un essai</b>!")));
		assertTrue(B_TAG_NO_BTAG.recognize(CharHelper.fromString("<b>un<em>es</b>sai</b>!")));
		assertFalse(B_TAG_NO_BTAG.recognize(CharHelper.fromString("<b>un  <b>essai</b>!")));
	}

	@Test
	public void test_contain_toto_and_titi() {
		RegularLanguageIF<Character> rec =
				inter(
						seq(star(any()), exactSequence('t','o','t', 'o'), star(any())),
						seq(star(any()), exactSequence('t', 'i', 't', 'i'), star(any()))
				);
		
		assertTrue(rec.recognize(CharHelper.fromString("un essai avec toto suivi de titit ahahah.")));
		assertTrue(rec.recognize(CharHelper.fromString("un essai avec titi suivi de toto ahahah.")));
		assertFalse(rec.recognize(CharHelper.fromString("un essai avec titi seul")));
		assertFalse(rec.recognize(CharHelper.fromString("un essai avec toto seul")));
		assertFalse(rec.recognize(CharHelper.fromString("un essai avec aucun")));
	}


	@Test
	public void test_contain_ele_and_leve() {
		RegularLanguageIF<Character> rec=
				inter(
						seq(star(any()), exactSequence('e','l','e'), star(any())),
						seq(star(any()), exactSequence('l','e','v'), star(any())));

		assertTrue(rec.recognize(CharHelper.fromString("un essai avec eleve.")));
		assertFalse(rec.recognize(CharHelper.fromString("un essai avec aucun")));
	}

}
