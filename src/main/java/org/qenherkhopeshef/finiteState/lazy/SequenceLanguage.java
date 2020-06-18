package org.qenherkhopeshef.finitestate.lazy;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Sequence language represents the concatenation of a number of languages.
 * 
 * Those languages might recognize the emptySequence string.
 * 
 * TODO : cache states to avoid multiple states creations (maybe not needed :
 * the sets automatically do it ?).
 * 
 * <h3>Saving partial results (ideas)</h3>
 * <p>
 * To implement saving partial results, the idea is that when a state is
 * reached, a vector of informations indicating how it was reached is given
 * alongside the state. As the same state might be reached in various different
 * ways, we need to keep only <b>one</b> of those vectors (the "best" one).
 * 
 * <p>
 * Making this complicates the interface, and is not really doable when the
 * automaton is embedded in another one (obviously, this has little meaning if
 * we are in a ComplementLanguage). So we will probably create an extension of
 * SequenceLanguage to do this, only at the "external" level.
 * 
 * suppose we are in a state for childIndex "k". So we have seen all children
 * from 0 to k-1. This state is associated with a vector v of length k-1, where
 * v[l] represents the <b>positions</b> <em>after</em> reading child l.
 * 
 * <p>
 * Two vectors of the same length are compared according to the lexicographical
 * order, and the lesser, the better (we favour early matches).
 * 
 * <p>
 * At each step, the recognizer will build a set of all states which can be
 * reached by reading tokens from 0 to n. Each state will be associated with its
 * "best" vector. In the accept method, we will get as input the previous "best"
 * vector for the current state (after reading token k-1), and for each output
 * state, we will either update or create an entry in the new state map. Note
 * that new values of the vector will only be produced when we reach terminal
 * states.
 * 
 * (note to self: maybe we must keep this map for all states, after all ???)
 * 
 * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
 */
class SequenceLanguage<T> extends CompositeLanguage<T> {

	/**
	 * Build a language corresponding to the sequence of L1...Ln where L1..Ln are all sub-languages in children.
	 * if children is emptySequence, build a sequence recognizing the emptyString language (not the emptySequence language).
	 * @param children
	 */
	public SequenceLanguage(List<? extends RegularLanguageIF<T>> children) {
		super(children.isEmpty()? Collections.singletonList(new EmptyStringLanguage<T>()): children);
	}

        @Override
	public Set<SequenceState> getInitialStates() {
		return statesStartingAt(0);
	}

	/**
	 * Returns the states for starting at a given place in the automaton.
	 * <p>
	 * The main reason is that the sub languages may recognize the emptySequence string.
	 * 
	 * @param childIndex
	 *            the starting position. Note that if childIndex is too large,
	 *            we return the emptySequence set.
	 * @return
	 */
	private Set<SequenceState> statesStartingAt(int childIndex) {
		HashSet<SequenceState> result = new HashSet<>();

		boolean nextReachable = true;

		while (nextReachable && childIndex < getChildren().size()) {
			nextReachable = false;
			for (MultiStateIF<T> s : getChildren().get(childIndex)
					.getInitialStates()) {
				result.add(new SequenceState(childIndex, s));
				if (s.isTerminal())
					nextReachable = true;
			}
			childIndex++;
		}
		return result;
	}

	/**
	 * The specific state class for sequence languages.
	 * <p> This state is used in the "normal" case of a non-emptySequence language sequence.
	 * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
	 */
	
	public class SequenceState extends AbstractMultiState<T> {
		private final int childIndex;
		private final MultiStateIF<T> childState;

		SequenceState(int childIndex, MultiStateIF<T> childState) {
			super(childState.isTerminal()
					&& childIndex == getChildren().size() - 1);
			this.childIndex = childIndex;
			this.childState = childState;
		}

		/**
		 * The index of the language currently being recognized.		
		 * @return
		 */
		public int getChildIndex() {
			return childIndex;
		}

		public Set<SequenceState> accept(T token) {
			// we are terminal if a) we contain a state whose owner is the last
			// child of the automaton
			// and b) this state is final.

			HashSet<SequenceState> result = new HashSet<>();

			Set<? extends MultiStateIF<T>> outStates = childState.accept(token);

			for (MultiStateIF<T> childState : outStates) {
				if (childState.isTerminal()) {
					result.addAll(statesStartingAt(childIndex + 1));
				}
				result.add(new SequenceState(childIndex, childState));
			}
			return result;
		}

		@Override
		/**
		 * True if the child state is terminal for the <em>Sequence language</em>
		 */
		public boolean isTerminal() {
			return childIndex == getChildren().size() - 1
					&& childState.isTerminal();
		}

		/**
		 * Does this state corresponds to the recognition of a child language ?
		 * 
		 * @return
		 */
		public boolean isTerminalForSubLanguage() {
			return childState.isTerminal();
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof SequenceLanguage.SequenceState) {
				@SuppressWarnings("unchecked")
				SequenceState s = (SequenceState) o;
				return this.getParent() == s.getParent()
						&& this.childIndex == s.childIndex
						&& this.childState.equals(s.childState);
			} else
				return false;
		}

		@Override
		public int hashCode() {
			return childIndex + 31 * childState.hashCode() + 31 * 31
					* getParent().hashCode();
		}

                @Override
		public RegularLanguageIF<T> getParent() {
			return SequenceLanguage.this;
		}

		@Override
		public String toString() {
			StringBuilder result = new StringBuilder("(state ");

			for (int i = 0; i < getChildren().size(); i++) {
				if (i == childIndex) {

					result.append("[");
					result.append(childState);
					result.append("]");
					if (childState.isTerminal()) {
						result.append("*");
					}
				} else {
					result.append(getChildren().get(i).toString());
					result.append(" ");
				}
			}
			result.append(")");
			return result.toString();
		}
	}

	@Override
	public String toString() {
		return "[S " + getChildren().toString() + "]";
	}
}
