package org.qenherkhopeshef.finitestate.lazy;

import java.util.*;

/**
 * An intersection language represents an intersection of languages.
 * 
 * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
 * @param <T> token type.
 */
class IntersectionLanguage<T> extends
		CompositeLanguage<T> {

	public IntersectionLanguage(List<? extends RegularLanguageIF<T>> children) {
		super(children);
	}

        @Override
	public Set<? extends MultiStateIF<T>> getInitialStates() {
		Set<IntersectionState> result;
		// Build all possible combinations of children states.
		// First build all possible states...
		ArrayList<ArrayList<MultiStateIF<T>>> allStates = new ArrayList<>();
		for (int i = 0; i < getChildren().size(); i++) {
			allStates.add(new ArrayList<>(getChildren().get(i)
					.getInitialStates()));
		}
		result = combineStates(allStates);
		return result;
	}

	/**
	 * Returns a cross product combination of all states.
	 * <p>
	 * We may wish to use a different system in the future.
	 * 
	 * @param result
	 * @param allStates
	 */
	private Set<IntersectionState> combineStates(
			ArrayList<ArrayList<MultiStateIF<T>>> allStates) {
		Set<IntersectionState> result = new HashSet<>();
		// Special case : if one of the state list is emptySequence, return an emptySequence
		// result for the combination
		for (int i = 0; i < allStates.size(); i++) {
			if (allStates.get(i).isEmpty())
				return result;
		}
		// There is at least one state for each child... we can combine them.
		// Combine the states...
		int[] indexes = new int[getChildren().size()];
		while (notLastIndex(indexes, allStates)) {
			ArrayList<MultiStateIF<T>> newChildState = new ArrayList<>();
			for (int i = 0; i < indexes.length; i++) {
				newChildState.add(allStates.get(i).get(indexes[i]));				
			}
			result.add(new IntersectionState(newChildState));
			nextIndex(indexes, allStates);
		}
		return result;
	}

	private void nextIndex(int[] indexes,
			ArrayList<ArrayList<MultiStateIF<T>>> allStates) {
		// The idea is to generate the sequence :
		// 0 0 1 / 0 0 2 ... 0 0 n-1/0 1 0/0 1 1/...
		// that is : we find the rightmost place which is not at the top value,
		// and we increment it.
		// at that point, all values to the right of this one are set to 0.
		// if everything is at top value, let the indexes be [n,0...0]
		int i = indexes.length - 1;
		while (i >= 0 && indexes[i] >= allStates.get(i).size() - 1) {
			i--;
		}
		if (i == -1) {
			// Last value...
			Arrays.fill(indexes, 0);
			indexes[0] = allStates.get(0).size();
		} else {
			// value i can be incremented :
			indexes[i]++;
			Arrays.fill(indexes, i + 1, indexes.length, 0);
		}
	}

	private boolean notLastIndex(int[] indexes,
			ArrayList<ArrayList<MultiStateIF<T>>> allStates) {
		return indexes[0] != allStates.get(0).size();
	}

	/**
	 * An intersection state is an "AND" between intersection states in the
	 * children.
	 * <p>
	 * Potentially, this is a place where problems can happen.
	 * <p>
	 * We have two solutions :
	 * <h3>First solution</h3>
	 * <p>
	 * consider that we keep only one state for each child (say childs a and b).
	 * Then, in the accept method, we would get states a1...an and b1...bm. The
	 * resulting states would be a cross product of those two results : (a1,b1),
	 * (a1,b2).... for n*m states.
	 * <h3>Second solution</h3>
	 * <p>
	 * keep all states in the intersection state. That is, for each child
	 * language, we have a set of possible states. Then, the result would be a
	 * large state, yet smaller than the cross product for the first solution.
	 * <p>
	 * The problem is that, in this case, we might miss some identical states.
	 * Two different Union state can overlap, and we miss this information.
	 * <p>
	 * ok, we are going to use the first option (a smart solution would be to
	 * swap between the two solutions depending on the number of results ?)
	 * 
	 * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
	 * 
	 */
	class IntersectionState extends AbstractMultiState<T> {

		private final List<MultiStateIF<T>> childrenStates;

		public IntersectionState(List<MultiStateIF<T>> childrenStates) {
			super(true);
			this.childrenStates = childrenStates;
			for (MultiStateIF<T> c : childrenStates) {
				if (!c.isTerminal())
					terminal = false;
			}
		}

                @Override
		public Set<IntersectionState> accept(T token) {
			Set<IntersectionState> result ;
			// Build all possible combinations of children states.
			// First build all possible states...
			ArrayList<ArrayList<MultiStateIF<T>>> allStates = new ArrayList<>();
			for (int i = 0; i < childrenStates.size(); i++) {
				allStates.add(new ArrayList<>(childrenStates
						.get(i).accept(token)));
			}
			result = combineStates(allStates);
			return result;
		}

		public RegularLanguageIF<T> getParent() {
			return IntersectionLanguage.this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof IntersectionLanguage.IntersectionState) {
				IntersectionState state = (IntersectionState) obj;
				if (!this.getParent().equals(state.getParent()))
					return false;
				else {
					boolean result = childrenStates.equals(state.childrenStates);
					return result;
				}
			} else
				return false;
		}

		@Override
		public int hashCode() {
			return childrenStates.hashCode() + 31 * getParent().hashCode();
		}

		@Override
		public String toString() {
			return "(Inter " + childrenStates + ")";
		}
	}

	@Override
	public String toString() {
		return "[Intersection " + getChildren() + "]";
	}
}
