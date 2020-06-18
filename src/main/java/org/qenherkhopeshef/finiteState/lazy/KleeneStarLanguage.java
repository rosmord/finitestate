package org.qenherkhopeshef.finitestate.lazy;

import java.util.HashSet;
import java.util.Set;

/**
 * Kleene star on another automaton.
 * 
 * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
 * @param <T> the token type.
 * 
 */
class KleeneStarLanguage<T> implements
		RegularLanguageIF<T> {

	private final RegularLanguageIF<T> repeatedAutomaton;

	public KleeneStarLanguage(RegularLanguageIF<T> repeatedAutomaton) {
		super();
		this.repeatedAutomaton = repeatedAutomaton;
	}

        @Override
	public Set<? extends MultiStateIF<T>> getInitialStates() {
		HashSet<MultiStateIF<T>> result = new HashSet<>();
		for (MultiStateIF<T> m : repeatedAutomaton.getInitialStates()) {
			result.add(new RepeatAutomatonState(m, true));
		}
		return result;
	}

	/**
	 * A state in the Kleene language. Basically, it's a state in the child
	 * language, with the following changes:
	 * <ul>
	 * <li>both initial and final states in the child language are terminal
	 * for the Kleene language.
	 * <li>When we get a terminal state of the child as a result of a token, we
	 * must return also an initial state. Note that the result must contain
	 * <b>both</b> entries, as they can accept different tokens (we are not
	 * <b>forced</b> to quit the kleen automaton when we reach a terminal state</li>
	 * </ul>
	 * <p> Note : we thought of considering the final state as non-terminal, but 
	 * it would not be correct when we need <em>all</em> terminal states, as when using complement.
	 * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
	 */
	private class RepeatAutomatonState extends SingleChildState<T> {

		public RepeatAutomatonState(MultiStateIF<T> childState, boolean terminal) {
			super(childState, terminal);
		}

                @Override
		public Set<? extends MultiStateIF<T>> accept(T token) {
			HashSet<MultiStateIF<T>> result = new HashSet<>();
			boolean hasInitial = false; // avoid adding the initial states twice
			// (they are always the same)

			for (MultiStateIF<T> newChildstate : childState.accept(token)) {
				result.add(new RepeatAutomatonState(newChildstate,
						newChildstate.isTerminal()));
				if (newChildstate.isTerminal() && !hasInitial) {
					result.addAll(getInitialStates());
					hasInitial = true;
				}
			}
			return result;
		}

                @Override
		public RegularLanguageIF<T> getParent() {
			return KleeneStarLanguage.this;
		}
	
		
		@Override
		public String toString() {
			return "(STAR "+ childState+ ")";
		}
	}
	
	public String toString() {
		return "[STAR"+ repeatedAutomaton.toString() +"]";
		
	}
}
