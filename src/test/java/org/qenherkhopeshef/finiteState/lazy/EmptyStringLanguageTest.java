package org.qenherkhopeshef.finiteState.lazy;

import org.junit.Test;
import org.qenherkhopeshef.finiteState.lazy.character.StringToListHelper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmptyStringLanguageTest {

	@Test
	public void testEmpy() {
		RegularLanguageIF<Character> rec = RegularLanguageFactory.emptySequence();
		assertTrue(rec.recognize(StringToListHelper.fromString("")));
	}
	
	@Test
	public void testNotEmpy() {
		RegularLanguageIF<Character> rec = RegularLanguageFactory.emptySequence();
		assertFalse(rec.recognize(StringToListHelper.fromString("A")));
	}
	

}
