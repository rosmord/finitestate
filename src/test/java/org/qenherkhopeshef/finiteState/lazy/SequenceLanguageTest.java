package org.qenherkhopeshef.finiteState.lazy;

import org.junit.Test;
import org.qenherkhopeshef.finiteState.lazy.character.CharacterLanguageFactory;
import org.qenherkhopeshef.finiteState.lazy.character.StringToListHelper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.qenherkhopeshef.finiteState.lazy.SampleAutomata.*;

import static org.qenherkhopeshef.finiteState.lazy.character.CharacterLanguageFactory.*;
import static org.qenherkhopeshef.finiteState.lazy.RegularLanguageFactory.*;

public class SequenceLanguageTest {


	@Test
	public void testABC() {
		RegularLanguageIF<Character> abc = CharacterLanguageFactory.c("abc");
		assertTrue(abc.recognize(StringToListHelper.fromString("abc")));
		assertFalse(abc.recognize(StringToListHelper.fromString("a")));
		assertFalse(abc.recognize(StringToListHelper.fromString("c")));
		assertFalse(abc.recognize(StringToListHelper.fromString("abcde")));
	}

	@Test
	public void testAEmptybEmpty() {
		assertTrue(A_EMPTY_B_EMPTY.recognize(StringToListHelper.fromString("ab")));
		assertFalse(A_EMPTY_B_EMPTY.recognize(StringToListHelper.fromString("a")));
		assertFalse(A_EMPTY_B_EMPTY.recognize(StringToListHelper.fromString("c")));
		assertFalse(A_EMPTY_B_EMPTY.recognize(StringToListHelper.fromString("abcde")));
	}

	@Test
	public void testAemptyAB() {
		assertTrue(A_EMPTY_A_B.recognize(StringToListHelper.fromString("aab")));
		assertFalse(A_EMPTY_A_B.recognize(StringToListHelper.fromString("ab")));
		assertFalse(A_EMPTY_A_B.recognize(StringToListHelper.fromString("aaab")));
		assertFalse(A_EMPTY_A_B.recognize(StringToListHelper.fromString("abcde")));
	}

	@Test
	public void testAbStarAC() {
		assertTrue(AB_STAR_AC.recognize(StringToListHelper.fromString("ac")));
		assertTrue(AB_STAR_AC.recognize(StringToListHelper.fromString("abac")));
		assertTrue(AB_STAR_AC.recognize(StringToListHelper.fromString("ababac")));
		assertTrue(AB_STAR_AC.recognize(StringToListHelper.fromString("abababac")));
		assertFalse(AB_STAR_AC.recognize(StringToListHelper.fromString("aac")));
		assertFalse(AB_STAR_AC.recognize(StringToListHelper.fromString("abc")));
	}

	@Test
	public void testaa_THEN_aStar_bc_inter_a_bcStar_THEN_bb() {
		assertTrue(AA_THEN_A_STAR_BC_INTER_A_BC_STAR_THEN_CC.recognize(StringToListHelper.fromString("aaabccc")));
		assertFalse(AA_THEN_A_STAR_BC_INTER_A_BC_STAR_THEN_CC.recognize(StringToListHelper.fromString("ABC")));
		assertFalse(AA_THEN_A_STAR_BC_INTER_A_BC_STAR_THEN_CC.recognize(StringToListHelper.fromString("A")));
		assertFalse(AA_THEN_A_STAR_BC_INTER_A_BC_STAR_THEN_CC.recognize(StringToListHelper.fromString("abcde")));
	}

	@Test
	public void testnot__ab_STAR_ac_ENDNOT_abab() {
		// A bit more tricky...
		assertTrue(NOT_AB_STAR_AC_ENDNOT_ABAB.recognize(StringToListHelper.fromString("abab")));
		assertTrue(NOT_AB_STAR_AC_ENDNOT_ABAB.recognize(StringToListHelper.fromString("asdfdsfdsfzfhzefabab")));
		assertFalse(NOT_AB_STAR_AC_ENDNOT_ABAB.recognize(StringToListHelper.fromString("acabab")));
		assertFalse(NOT_AB_STAR_AC_ENDNOT_ABAB.recognize(StringToListHelper.fromString("abacabab")));
	}

	@Test
	public void testb_tag() {
		assertTrue(B_TAG_NO_BTAG.recognize(StringToListHelper.fromString("<b>un essai</b>!")));
		assertTrue(B_TAG_NO_BTAG.recognize(StringToListHelper.fromString("<b>un<em>es</b>sai</b>!")));
		assertFalse(B_TAG_NO_BTAG.recognize(StringToListHelper.fromString("<b>un  <b>essai</b>!")));
	}

	@Test
	public void test_contain_toto_and_titi() {
		RegularLanguageIF<Character> rec =
				inter(
						seq(star(any()), c("toto"), star(any())),
						seq(star(any()), c("titi"), star(any()))
				);
		
		assertTrue(rec.recognize(StringToListHelper.fromString("un essai avec toto suivi de titit ahahah.")));
		assertTrue(rec.recognize(StringToListHelper.fromString("un essai avec titi suivi de toto ahahah.")));
		assertFalse(rec.recognize(StringToListHelper.fromString("un essai avec titi seul")));
		assertFalse(rec.recognize(StringToListHelper.fromString("un essai avec toto seul")));
		assertFalse(rec.recognize(StringToListHelper.fromString("un essai avec aucun")));
	}


	@Test
	public void test_contain_ele_and_leve() {
		RegularLanguageIF<Character> rec=
				inter(
						seq(star(any()), c("ele"), star(any())),
						seq(star(any()), c("lev"), star(any())));

		assertTrue(rec.recognize(StringToListHelper.fromString("un essai avec eleve.")));
		assertFalse(rec.recognize(StringToListHelper.fromString("un essai avec aucun")));
	}

}
