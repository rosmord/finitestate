package org.qenherkhopeshef.finiteState.lazy;

import org.junit.Test;
import org.qenherkhopeshef.finiteState.lazy.character.CharacterLanguageFactory;
import org.qenherkhopeshef.finiteState.lazy.character.StringToListHelper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LabelLanguageTest {

	@Test
	public void testA_A() {
		RegularLanguageIF<Character> rec = CharacterLanguageFactory.c('a');
		assertTrue(rec.recognize(StringToListHelper.fromString("a")));
	}

	@Test
	public void testC_AZ() {
		RegularLanguageIF<Character> rec = CharacterLanguageFactory.c('a', 'z');
		assertTrue(rec.recognize(StringToListHelper.fromString("c")));
		assertTrue(rec.recognize(StringToListHelper.fromString("a")));
		assertTrue(rec.recognize(StringToListHelper.fromString("z")));
		assertFalse(rec.recognize(StringToListHelper.fromString("+")));
	}

	@Test
	public void testA_B() {
		RegularLanguageIF<Character> rec = CharacterLanguageFactory.c('a');
		assertFalse(rec.recognize(StringToListHelper.fromString("b")));
	}

	@Test
	public void testA_ZERO() {
		RegularLanguageIF<Character> rec = CharacterLanguageFactory.c('a');
		assertFalse(rec.recognize(StringToListHelper.fromString("")));
	}

}
