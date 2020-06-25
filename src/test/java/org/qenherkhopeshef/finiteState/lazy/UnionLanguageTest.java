package org.qenherkhopeshef.finitestate.lazy;

import org.qenherkhopeshef.finitestate.lazy.RegularLanguageIF;
import static org.junit.Assert.*;

import org.junit.Test;

import static org.qenherkhopeshef.finitestate.lazy.SampleAutomata.*;

public class UnionLanguageTest {


	@Test
	public void testAB_OR_AC() {
		RegularLanguageIF<Character> rec =  AB_OR_AC;
		assertTrue(rec.recognize(CharHelper.fromString("ab")));
		assertTrue(rec.recognize(CharHelper.fromString("ac")));
		assertFalse(rec.recognize(CharHelper.fromString("")));
		assertFalse(rec.recognize(CharHelper.fromString("abc")));
	}

	@Test
	public void testAB_OR_AC_BB() {
		RegularLanguageIF<Character> rec = AB_OR_AC_BB;
		assertTrue(rec.recognize(CharHelper.fromString("abbb")));
		assertTrue(rec.recognize(CharHelper.fromString("acbb")));

		assertFalse(rec.recognize(CharHelper.fromString("ab")));
		assertFalse(rec.recognize(CharHelper.fromString("abbbbb")));

		assertFalse(rec.recognize(CharHelper.fromString("ac")));
		assertFalse(rec.recognize(CharHelper.fromString("")));
		assertFalse(rec.recognize(CharHelper.fromString("abc")));
	
	}

}
