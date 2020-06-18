package org.qenherkhopeshef.finitestate.lazy;

import static org.junit.Assert.*;

import org.junit.Test;
import org.qenherkhopeshef.finitestate.lazy.character.StringToListHelper;

import java.util.List;

public class IntersectionLanguageTest {


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
