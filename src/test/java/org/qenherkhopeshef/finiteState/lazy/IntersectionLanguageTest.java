package org.qenherkhopeshef.finiteState.lazy;

import static org.junit.Assert.*;

import org.junit.Test;
import org.qenherkhopeshef.finiteState.lazy.character.CharacterLanguageFactory;
import org.qenherkhopeshef.finiteState.lazy.character.StringToListHelper;

import java.util.List;

public class IntersectionLanguageTest {

	@Test
	public void testSimpleInter() {
		RegularLanguageIF<Character> l = RegularLanguageFactory.inter(CharacterLanguageFactory.c('A'), CharacterLanguageFactory.c(('B')));
		assertFalse(l.recognize(StringToListHelper.fromString("A")));
		assertFalse(l.recognize(StringToListHelper.fromString("B")));
	}

	@Test
	public void testSimpleInterSeq() {

		RegularLanguageIF<Character> l = RegularLanguageFactory.inter(
				RegularLanguageFactory.seq(CharacterLanguageFactory.c('A')),
				RegularLanguageFactory.seq(CharacterLanguageFactory.c(('B'))));

		assertFalse(l.recognize(StringToListHelper.fromString("A")));
		assertFalse(l.recognize(StringToListHelper.fromString("B")));
	}

	@Test
	public void testASTARB_C_INTER_A_BCSTAR() {
		// (A*bc) inter (A(bc)*)
		RegularLanguageIF<Character> rec = SampleAutomata.A_STAR_BC_INTER_A_BC_STAR;
		assertTrue(rec.recognize(StringToListHelper.fromString("abc")));
		assertFalse(rec.recognize(StringToListHelper.fromString("aabc")));
		assertFalse(rec.recognize(StringToListHelper.fromString("aaaaabc")));
		assertFalse(rec.recognize(StringToListHelper.fromString("abcbcbcbc")));
		RegularExtractor<Character> regularExtractor = new RegularExtractor<Character>(RegularLanguageFactory.skip(),rec);
		List<List<Integer>> res = regularExtractor.search(StringToListHelper.fromString("abc"));
		System.err.println(res);
	}

}
