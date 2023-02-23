package org.qenherkhopeshef.finitestate.lazy;

import java.util.Collections;
import java.util.Set;

/**
 * A regular language recognizing a single class of tokens.
 * 
 * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
 * @param <T> the token type.
 */
class LabelLanguage<T> implements RegularLanguageIF<T> {
	private final LazyLabelIF<T> label;

	public LabelLanguage(LazyLabelIF<T> label) {
		super();
		this.label = label;
	}

	@Override
	public Set<? extends MultiStateIF<T>> getInitialStates() {
		return Collections.singleton(new TokenAutomatonState((short) 0));
	}

	class TokenAutomatonState implements MultiStateIF<T> {

		// This particular automaton has 3 states : 0, initial state, 1, after the label
		// has been recognized, and 2,
		// a token not recognized by the label was seen.
		// possible transitions are from 0 to 1 (the label was recognized), and all
		// other send in state 2.
		private short state = 0;

		public TokenAutomatonState(short state) {
			super();
			this.state = state;
		}

		@Override
		public boolean isTerminal() {
			return state == 1;
		}

		@Override
		public java.util.Set<? extends MultiStateIF<T>> accept(T token) {
			short out = 2;
			if (state == 0 && label.matches(token)) {
				out = 1;
			}
			return Collections.singleton(new TokenAutomatonState(out));
		}

		@Override
		public RegularLanguageIF<T> getParent() {
			return LabelLanguage.this;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof LabelLanguage.TokenAutomatonState) {
				// Need for wildcard '?' to avoid unchecked warning.
				LabelLanguage<?>.TokenAutomatonState other 
					= (LabelLanguage<?>.TokenAutomatonState) obj;
				return this.getParent().equals(other.getParent()) && this.state == other.state;
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			return this.getParent().hashCode() + 31 * state;
		}

		@Override
		public String toString() {
			String[] s = { "start", "ok", "bad" };
			return "(label " + label.toString() + s[state] + ")";
		}
	}

	@Override
	public String toString() {
		return label.toString();
	}
}
