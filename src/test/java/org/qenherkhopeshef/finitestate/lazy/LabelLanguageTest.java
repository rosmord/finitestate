package org.qenherkhopeshef.finitestate.lazy;

import org.qenherkhopeshef.finitestate.lazy.RegularLanguageIF;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LabelLanguageTest {

	@Test
	public void testA_A() {
		RegularLanguageIF<Character> rec = RegularLanguageFactory.exact('a');
		assertTrue(rec.recognize(CharHelper.fromString("a")));
	}

	@Test
	public void testC_AZ() {
		RegularLanguageIF<Character> rec = RegularLanguageFactory.range('a', 'z'); 						
		assertTrue(rec.recognize(CharHelper.fromString("c")));
		assertTrue(rec.recognize(CharHelper.fromString("a")));
		assertTrue(rec.recognize(CharHelper.fromString("z")));
		assertFalse(rec.recognize(CharHelper.fromString("+")));
	}

	@Test
	public void testA_B() {
		RegularLanguageIF<Character> rec = RegularLanguageFactory.exact('a');					
		assertFalse(rec.recognize(CharHelper.fromString("b")));
	}

	@Test
	public void testA_ZERO() {
		RegularLanguageIF<Character> rec = RegularLanguageFactory.exact('a');
		assertFalse(rec.recognize(CharHelper.fromString("")));
	}

}
