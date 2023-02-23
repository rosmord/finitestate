package org.qenherkhopeshef.finitestate.lazy;

import java.util.Collections;
import java.util.Set;

/**
 * The emptySequence language (i.e. the language which recognizes <em>nothing</em>.
 * Do not confuse with the "emptySequence string language", which recognizes the string "".
 * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
 * @param <T> token type.
 */
class EmptyLanguage<T> implements RegularLanguageIF<T>{

	@Override
	public Set<? extends MultiStateIF<T>> getInitialStates() {
		return Collections.emptySet();
	}

}
