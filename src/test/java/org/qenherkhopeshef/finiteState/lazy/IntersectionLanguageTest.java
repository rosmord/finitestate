package org.qenherkhopeshef.finitestate.lazy;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;

public class IntersectionLanguageTest {


	@Test
	public void testASTARB_C_INTER_A_BCSTAR() {
		// (A*bc) inter (A(bc)*)
		RegularLanguageIF<Character> rec = SampleAutomata.A_STAR_BC_INTER_A_BC_STAR;
		assertTrue(rec.recognize(CharHelper.fromString("abc")));
		assertFalse(rec.recognize(CharHelper.fromString("aabc")));
		assertFalse(rec.recognize(CharHelper.fromString("aaaaabc")));
		assertFalse(rec.recognize(CharHelper.fromString("abcbcbcbc")));
		RegularExtractor<Character> regularExtractor = new RegularExtractor<Character>(RegularLanguageFactory.skip(),rec);
		List<List<Integer>> res = regularExtractor.search(CharHelper.fromString("abc"));
		System.err.println(res);
	}

}
