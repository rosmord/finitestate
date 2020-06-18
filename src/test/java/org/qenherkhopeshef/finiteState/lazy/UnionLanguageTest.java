package org.qenherkhopeshef.finitestate.lazy;

import org.qenherkhopeshef.finitestate.lazy.RegularLanguageIF;
import static org.junit.Assert.*;

import org.junit.Test;
import org.qenherkhopeshef.finitestate.lazy.character.StringToListHelper;

import static org.qenherkhopeshef.finitestate.lazy.SampleAutomata.*;

public class UnionLanguageTest {


	@Test
	public void testAB_OR_AC() {
		RegularLanguageIF<Character> rec =  AB_OR_AC;
		assertTrue(rec.recognize(StringToListHelper.fromString("ab")));
		assertTrue(rec.recognize(StringToListHelper.fromString("ac")));
		assertFalse(rec.recognize(StringToListHelper.fromString("")));
		assertFalse(rec.recognize(StringToListHelper.fromString("abc")));
	}

	@Test
	public void testAB_OR_AC_BB() {
		RegularLanguageIF<Character> rec = AB_OR_AC_BB;
		assertTrue(rec.recognize(StringToListHelper.fromString("abbb")));
		assertTrue(rec.recognize(StringToListHelper.fromString("acbb")));

		assertFalse(rec.recognize(StringToListHelper.fromString("ab")));
		assertFalse(rec.recognize(StringToListHelper.fromString("abbbbb")));

		assertFalse(rec.recognize(StringToListHelper.fromString("ac")));
		assertFalse(rec.recognize(StringToListHelper.fromString("")));
		assertFalse(rec.recognize(StringToListHelper.fromString("abc")));
	
	}

}
