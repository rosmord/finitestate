package org.qenherkhopeshef.finiteState.lazy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A union of languages.
 * <p> Implements the "or/Union" operation.
 * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
 * @param <T> the token type.
 */
class UnionLanguage<T> extends CompositeLanguage<T> {

	public UnionLanguage(List<RegularLanguageIF<T>> children) {
		super(children);
	}

        @Override
	public Set<? extends MultiStateIF<T>> getInitialStates() {
		Set<MultiStateIF<T>> result= new HashSet<>();
		for (RegularLanguageIF<T> subLang: getChildren()) {
			Set<? extends MultiStateIF<T>> childStates = subLang.getInitialStates();
			for (MultiStateIF<T> childState: childStates) {
				result.add(new UnionState(childState, childState.isTerminal()));
			}
		}
		return result;
	}

	
	/**
	 * A union state is a simple wrapper around a state in one particular child.
	 * <p> This is not very intuitive, but is correct: as both getInialState() and accept() return 
	 * sets of possible states, implicitely linked with an "or", the getInitialState() method performs the initial branching 
	 * between the possible child languages, and the rest is done on one language only.
	 * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
	 *
	 */
	private class UnionState extends SingleChildState<T> {

		public UnionState(MultiStateIF<T> childState, boolean terminal) {
			super(childState, terminal);			
		}

                @Override
		public Set<UnionState> accept(T token) {
			Set<UnionLanguage<T>.UnionState> result= new HashSet<>();
			for (MultiStateIF<T> nextChildState: childState.accept(token)) {
				result.add(new UnionState(nextChildState, nextChildState.isTerminal()));
			}
			return result;
		}

		@Override
		public String toString() {
			return "[U "+ childState.toString() +"]";
		}
		
		public RegularLanguageIF<T> getParent() {
			return UnionLanguage.this;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder buff=new StringBuilder();
		boolean first= true;
		for (RegularLanguageIF<T> l: getChildren()) {
			if (! first) {
				buff.append(" | ");
			}
			buff.append(l);
			first= false;				
		}
		return buff.toString();
	}
		
}
