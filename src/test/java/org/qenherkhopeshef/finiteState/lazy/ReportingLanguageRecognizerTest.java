package org.qenherkhopeshef.finiteState.lazy;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.qenherkhopeshef.finiteState.lazy.character.CharacterLanguageFactory;
import org.qenherkhopeshef.finiteState.lazy.character.StringToListHelper;

import static org.junit.Assert.*;
import static org.qenherkhopeshef.finiteState.lazy.RegularLanguageFactory.*;

public class ReportingLanguageRecognizerTest {
	@Test
	public void simpleTest() {
		ReportingLanguageRecognizer<Character> rec = new ReportingLanguageRecognizer<>(
				CharacterLanguageFactory.c('0', '9'),
				CharacterLanguageFactory.c('a'),
				CharacterLanguageFactory.c('b')
		);
		assertTrue(rec.recognize(StringToListHelper.fromString("3ab")));
		assertEquals(Arrays.asList(1, 2, 3), rec.getMarkers());
	}

	@Test
	public void simpleTestRep() {
		ReportingLanguageRecognizer<Character> rec = new ReportingLanguageRecognizer<>(
				RegularLanguageFactory.star(CharacterLanguageFactory.c('a')),
				CharacterLanguageFactory.c('b')
		);
		rec.recognize(StringToListHelper.fromString("ab"));
		assertEquals(Arrays.asList(1, 2), rec.getMarkers());
		rec.recognize(StringToListHelper.fromString("aaab"));
		assertEquals(Arrays.asList(3, 4), rec.getMarkers());
	}

	@Test
	public void testFindNumber() {
		RegularLanguageIF<Character> numberRegexp = RegularLanguageFactory.seq(
				RegularLanguageFactory.opt(CharacterLanguageFactory.c('-')),
				RegularLanguageFactory.plus(CharacterLanguageFactory.c('0', '9')),
				RegularLanguageFactory.opt(RegularLanguageFactory.seq(CharacterLanguageFactory.c('.'),
						RegularLanguageFactory.plus(CharacterLanguageFactory.c('0', '9')))));

		ReportingLanguageRecognizer<Character> rec = new ReportingLanguageRecognizer<>(
				RegularLanguageFactory.skip(), numberRegexp, CharacterLanguageFactory.c(' '),
				RegularLanguageFactory.skip(), CharacterLanguageFactory.c('$')
		);

		assertTrue(rec.recognize(StringToListHelper.fromString("un essai 12334.34 dsfsdfd$")));
		assertTrue(rec.recognize(StringToListHelper.fromString("un essai 12334 dsfsdfd$")));
		assertTrue(rec.recognize(StringToListHelper.fromString("un essai -12334 dsfsdf$")));
		assertFalse(rec.recognize(StringToListHelper.fromString("un essai -12334$")));
		assertFalse(rec.recognize(StringToListHelper.fromString("un essai$")));

		assertTrue(rec.recognize(StringToListHelper.fromString("et -233.66 et 3.1416 !!$")));
		assertEquals(Arrays.asList(3, 10, 11, 23, 24), rec.getMarkers());
		// Test with un-matching pattern
		boolean result = rec.recognize(StringToListHelper.fromString("avec que du texte"));
		assertFalse(result);
		assertEquals(-1, rec.endingPosition());
	}

	@Test
	public void testEmpty() {
		RegularLanguageIF<Character> r0 = RegularLanguageFactory.seq();
		assertTrue("Empty is recognized", r0.recognize(StringToListHelper.fromString("")));
		ReportingLanguageRecognizer<Character> recognizer = new ReportingLanguageRecognizer<Character>(r0);
		boolean b = recognizer.recognize(StringToListHelper.fromString("un essai"));
		assertTrue("Empty is recognized", b);
		assertEquals(Arrays.asList(0), recognizer.getMarkers());
	}

	/**
	 * Tests if 'a' union 'b' language is recognized.
	 * Note : this test fails if "earlyStop" is set to false in the recognizer class.
	 * We must have a closer look at the code and see how the semantics can be chosen.
	 */
	@Test
	public void testUnion() {
		RegularLanguageIF<Character> l = RegularLanguageFactory.seq(
				RegularLanguageFactory.or(CharacterLanguageFactory.c('a'), CharacterLanguageFactory.c('b'))
		);

		ReportingLanguageRecognizer<Character> rec = new ReportingLanguageRecognizer<Character>(l);
		boolean res = rec.recognize(StringToListHelper.fromString("abcd"));
		assertTrue(res);
		assertEquals(1, rec.getMarkers().get(0).intValue());
		assertEquals(1, rec.endingPosition());
		res = rec.recognize(StringToListHelper.fromString("bcd"));
		assertTrue(res);
		assertEquals(1, rec.getMarkers().get(0).intValue());
		assertEquals(1, rec.endingPosition());

	}


	@Test
	public void integerExtractTest() {
		String text = "il y a 100 manières de travailler 2 à 4.";
		ReportingLanguageRecognizer<Character> rep= new ReportingLanguageRecognizer<Character>(
				skip(),
				plus(range('0', '9'))
		);
		assertTrue(rep.recognize(StringToListHelper.fromString(text)));
		List<Integer> res = rep.getMarkers();
		assertEquals(2, res.size());
		assertEquals("1", text.substring(res.get(0), res.get(1)));
	
	}

}
