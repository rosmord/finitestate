package org.qenherkhopeshef.finiteState.lazy.lexiconBased;

import org.qenherkhopeshef.finiteState.lazy.RegularLanguageFactory;
import org.qenherkhopeshef.finiteState.lazy.RegularLanguageIF;

public final class DemoWordLabelHelper  {

	private DemoWordLabelHelper() {
	}

	public static RegularLanguageIF<DemoWord> textLabel(String text) {
		return RegularLanguageFactory.label(demoWord -> demoWord.getWord().equals(text));
	}

	public static RegularLanguageIF<DemoWord> posLabel(String partOfSpeech) {
			return RegularLanguageFactory.label(demoWord -> demoWord.getPartOfSpeech().equals(partOfSpeech));
		}
}
