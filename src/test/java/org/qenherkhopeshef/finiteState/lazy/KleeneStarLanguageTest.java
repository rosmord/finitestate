package org.qenherkhopeshef.finitestate.lazy;

import org.qenherkhopeshef.finitestate.lazy.RegularLanguageIF;
import org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory;
import static org.junit.Assert.*;

import org.junit.Test;
import org.qenherkhopeshef.finitestate.lazy.character.CharacterLanguageFactory;
import org.qenherkhopeshef.finitestate.lazy.character.StringToListHelper;

import static org.qenherkhopeshef.finitestate.lazy.SampleAutomata.*;

public class KleeneStarLanguageTest {


	@Test
	public void testAStar() {
		assertTrue(A_STAR.recognize(StringToListHelper.fromString("")));
		assertTrue(A_STAR.recognize(StringToListHelper.fromString("a")));
		assertTrue(A_STAR.recognize(StringToListHelper.fromString("aa")));
		assertTrue(A_STAR.recognize(StringToListHelper.fromString("aaa")));
		assertFalse(A_STAR.recognize(StringToListHelper.fromString("aaba")));
		assertFalse(A_STAR.recognize(StringToListHelper.fromString("aabb")));
		assertFalse(A_STAR.recognize(StringToListHelper.fromString("c")));
	}

	@Test
	public void testAStarBC() {
		assertTrue(A_STAR_BC.recognize(StringToListHelper.fromString("abc")));
		assertTrue(A_STAR_BC.recognize(StringToListHelper.fromString("aaaabc")));
		assertTrue(A_STAR_BC.recognize(StringToListHelper.fromString("bc")));
		assertFalse(A_STAR_BC.recognize(StringToListHelper.fromString("ab")));


	}

	@Test
	public void testA_BCStar() {
		assertTrue(A_BC_STAR.recognize(StringToListHelper.fromString("abc")));
		assertTrue(A_BC_STAR.recognize(StringToListHelper.fromString("abcbc")));
		assertTrue(A_BC_STAR.recognize(StringToListHelper.fromString("a")));
		assertFalse(A_BC_STAR.recognize(StringToListHelper.fromString("ab")));
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
												CharacterLanguageFactory.c('a'),
												CharacterLanguageFactory.c('b'))),

										CharacterLanguageFactory.c('a'),
										CharacterLanguageFactory.c("c")
								)),
						CharacterLanguageFactory.c('b'),
						CharacterLanguageFactory.c('b'),
						CharacterLanguageFactory.c('b'));
		assertTrue(rec.recognize(StringToListHelper.fromString("bbb")));
		assertTrue(rec.recognize(StringToListHelper.fromString("acbbb")));
		assertTrue(rec.recognize(StringToListHelper.fromString("ababacbbb")));
		assertTrue(rec.recognize(StringToListHelper.fromString("acabacababacbbb")));
		assertFalse(rec.recognize(StringToListHelper.fromString("acbb")));
		assertFalse(rec.recognize(StringToListHelper.fromString("abbbb")));
	}
}
