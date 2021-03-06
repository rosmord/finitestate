package org.qenherkhopeshef.finitestate.lazy;

import org.qenherkhopeshef.finitestate.lazy.RegularLanguageIF;
import org.qenherkhopeshef.finitestate.lazy.ReportingLanguageRecognizer;
import org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory.*;

public class ReportingLanguageRecognizerTest {
	@Test
	public void simpleTest() {
		ReportingLanguageRecognizer<Character> rec = new ReportingLanguageRecognizer<>(
				RegularLanguageFactory.range('0', '9'),
				RegularLanguageFactory.exact('a'),
				RegularLanguageFactory.exact('b')
		);
		assertTrue(rec.recognize(CharHelper.fromString("3ab")));
		assertEquals(Arrays.asList(1, 2, 3), rec.getMarkers());
	}

	@Test
	public void simpleTestRep() {
		ReportingLanguageRecognizer<Character> rec = new ReportingLanguageRecognizer<>(
				RegularLanguageFactory.star(RegularLanguageFactory.exact('a')),
				RegularLanguageFactory.exact('b')
		);
		rec.recognize(CharHelper.fromString("ab"));
		assertEquals(Arrays.asList(1, 2), rec.getMarkers());
		rec.recognize(CharHelper.fromString("aaab"));
		assertEquals(Arrays.asList(3, 4), rec.getMarkers());
	}

	@Test
	public void testFindNumber() {
		RegularLanguageIF<Character> numberRegexp = RegularLanguageFactory.seq(
				RegularLanguageFactory.opt(RegularLanguageFactory.exact('-')),
				RegularLanguageFactory.plus(RegularLanguageFactory.range('0', '9')),
				RegularLanguageFactory.opt(RegularLanguageFactory.seq(RegularLanguageFactory.exact('.'),
						RegularLanguageFactory.plus(RegularLanguageFactory.range('0', '9')))));

		ReportingLanguageRecognizer<Character> rec = new ReportingLanguageRecognizer<>(
				RegularLanguageFactory.skip(), numberRegexp, RegularLanguageFactory.exact(' '),
				RegularLanguageFactory.skip(), RegularLanguageFactory.exact('$')
		);

		assertTrue(rec.recognize(CharHelper.fromString("un essai 12334.34 dsfsdfd$")));
		assertTrue(rec.recognize(CharHelper.fromString("un essai 12334 dsfsdfd$")));
		assertTrue(rec.recognize(CharHelper.fromString("un essai -12334 dsfsdf$")));
		assertFalse(rec.recognize(CharHelper.fromString("un essai -12334$")));
		assertFalse(rec.recognize(CharHelper.fromString("un essai$")));

		assertTrue(rec.recognize(CharHelper.fromString("et -233.66 et 3.1416 !!$")));
		assertEquals(Arrays.asList(3, 10, 11, 23, 24), rec.getMarkers());
		// Test with un-matching pattern
		boolean result = rec.recognize(CharHelper.fromString("avec que du texte"));
		assertFalse(result);
		assertEquals(-1, rec.endingPosition());
	}

	@Test
	public void testEmpty() {
		RegularLanguageIF<Character> r0 = RegularLanguageFactory.seq();
		assertTrue("Empty is recognized", r0.recognize(CharHelper.fromString("")));
		ReportingLanguageRecognizer<Character> recognizer = new ReportingLanguageRecognizer<Character>(r0);
		boolean b = recognizer.recognize(CharHelper.fromString("un essai"));
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
				RegularLanguageFactory.union(RegularLanguageFactory.exact('a'), RegularLanguageFactory.exact('b'))
		);

		ReportingLanguageRecognizer<Character> rec = new ReportingLanguageRecognizer<Character>(l);
		boolean res = rec.recognize(CharHelper.fromString("abcd"));
		assertTrue(res);
		assertEquals(1, rec.getMarkers().get(0).intValue());
		assertEquals(1, rec.endingPosition());
		res = rec.recognize(CharHelper.fromString("bcd"));
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
		assertTrue(rep.recognize(CharHelper.fromString(text)));
		List<Integer> res = rep.getMarkers();
		assertEquals(2, res.size());
		assertEquals("1", text.substring(res.get(0), res.get(1)));
	
	}

}
