package org.qenherkhopeshef.finitestate.lazy;

import org.qenherkhopeshef.finitestate.lazy.RegularLanguageIF;
import org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory;
import static org.junit.Assert.*;

import org.junit.Test;

import static org.qenherkhopeshef.finitestate.lazy.SampleAutomata.*;

public class KleeneStarLanguageTest {


	@Test
	public void testAStar() {
		assertTrue(A_STAR.recognize(CharHelper.fromString("")));
		assertTrue(A_STAR.recognize(CharHelper.fromString("a")));
		assertTrue(A_STAR.recognize(CharHelper.fromString("aa")));
		assertTrue(A_STAR.recognize(CharHelper.fromString("aaa")));
		assertFalse(A_STAR.recognize(CharHelper.fromString("aaba")));
		assertFalse(A_STAR.recognize(CharHelper.fromString("aabb")));
		assertFalse(A_STAR.recognize(CharHelper.fromString("c")));
	}

	@Test
	public void testAStarBC() {
		assertTrue(A_STAR_BC.recognize(CharHelper.fromString("abc")));
		assertTrue(A_STAR_BC.recognize(CharHelper.fromString("aaaabc")));
		assertTrue(A_STAR_BC.recognize(CharHelper.fromString("bc")));
		assertFalse(A_STAR_BC.recognize(CharHelper.fromString("ab")));


	}

	@Test
	public void testA_BCStar() {
		assertTrue(A_BC_STAR.recognize(CharHelper.fromString("abc")));
		assertTrue(A_BC_STAR.recognize(CharHelper.fromString("abcbc")));
		assertTrue(A_BC_STAR.recognize(CharHelper.fromString("a")));
		assertFalse(A_BC_STAR.recognize(CharHelper.fromString("ab")));
	}

	/**
	 * Complex embedded automaton : (ab*c)*bbb)
	 */
	@Test
	public void testEmbedded() {
		RegularLanguageIF<Character> rec =
				RegularLanguageFactory.seq(
						RegularLanguageFactory.star(
								RegularLanguageFactory.seq(
										RegularLanguageFactory.star(
												RegularLanguageFactory.seq(
														RegularLanguageFactory.exact('a'),
														RegularLanguageFactory.exact('b'))),

										RegularLanguageFactory.exact('a'),
										RegularLanguageFactory.exact('c')
								)),
						RegularLanguageFactory.exact('b'),
						RegularLanguageFactory.exact('b'),
						RegularLanguageFactory.exact('b'));
		assertTrue(rec.recognize(CharHelper.fromString("bbb")));
		assertTrue(rec.recognize(CharHelper.fromString("acbbb")));
		assertTrue(rec.recognize(CharHelper.fromString("ababacbbb")));
		assertTrue(rec.recognize(CharHelper.fromString("acabacababacbbb")));
		assertFalse(rec.recognize(CharHelper.fromString("acbb")));
		assertFalse(rec.recognize(CharHelper.fromString("abbbb")));
	}
}
