package org.qenherkhopeshef.finitestate.lazy;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for languages which are made of other languages.
 * @author rosmord
 * @param <T> 
 */
abstract class CompositeLanguage<T> implements RegularLanguageIF<T> {
	private final List<RegularLanguageIF<T>> children;

	/**
	 * Create a language made of the languages in children.
	 * @param children
	 */
	public CompositeLanguage(List<? extends RegularLanguageIF<T>> children) {
		super();
		this.children = new ArrayList<>(children);
	}

	protected List<RegularLanguageIF<T>> getChildren() {
		return children;
	}
	
	
}
