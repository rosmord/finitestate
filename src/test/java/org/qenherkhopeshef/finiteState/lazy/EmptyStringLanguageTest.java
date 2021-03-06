package org.qenherkhopeshef.finitestate.lazy;

import org.qenherkhopeshef.finitestate.lazy.RegularLanguageIF;
import org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmptyStringLanguageTest {

	@Test
	public void testEmpy() {
		RegularLanguageIF<Character> rec = RegularLanguageFactory.emptySequence();
		assertTrue(rec.recognize(CharHelper.fromString("")));
	}
	
	@Test
	public void testNotEmpy() {
		RegularLanguageIF<Character> rec = RegularLanguageFactory.emptySequence();
		assertFalse(rec.recognize(CharHelper.fromString("A")));
	}
}
