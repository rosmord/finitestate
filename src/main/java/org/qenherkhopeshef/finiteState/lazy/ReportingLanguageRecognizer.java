package org.qenherkhopeshef.finitestate.lazy;

import java.util.*;
import java.util.Map.Entry;

/**
 * A specific language recognizer, which reports the limits where expressions
 * from the sublanguages where found.
 * <p>
 * This class is stateful, and hence not shareable. It's not public : the actual
 * class to use is {@link RegularExtractor}
 * <p>
 * <p>
 * TODO : have a closer look to "early stop". Currently, the system semantics regarding
 * what is matched are not that clear, and only well defined for earlyStop = true.
 *
 * @param <T> the language to recognize.
 * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
 * @see RegularExtractor for the facade to this class.
 */
class ReportingLanguageRecognizer<T> {

	private final SequenceLanguage<T> sequenceLanguage;
	private List<Integer> markers = null;
	private boolean terminal;
	private boolean earlyStop = true;

	public ReportingLanguageRecognizer(List<RegularLanguageIF<T>> regularLanguage) {
		this.sequenceLanguage = new SequenceLanguage<>(new ArrayList<>(regularLanguage));
	}

	public ReportingLanguageRecognizer(RegularLanguageIF<T>... languages) {
		List<RegularLanguageIF<T>> l = Arrays.asList(languages);
		this.sequenceLanguage = new SequenceLanguage<T>(l);
	}


	/**
	 * Chooses if we want to stop as soon as possible.
	 * Defaults to true.
	 *
	 * @param earlyStop
	 */
	public void setEarlyStop(boolean earlyStop) {
		this.earlyStop = earlyStop;
	}


	/**
	 * Do we seek the shortest match (default) or to match all tokens ?
	 *
	 * @return
	 */
	public boolean isEarlyStop() {
		return earlyStop;
	}

	/**
	 * Recognizes a list of token.
	 * <p>
	 * If this method returns true, you can retrieve the position of the end of
	 * the recognized sequence, plus a list of markers indicating exactly what
	 * was recognized by the various elements.
	 *
	 * <p>
	 * About markers: if the language is L1,L2,...Ln,
	 * <p>
	 * the marker vector v1,...vn contains that ending position of the text
	 * recognized by language Li.
	 *
	 * @param tokens
	 * @return a boolean, true if the result has been recognised.
	 */
	public boolean recognize(List<T> tokens) {
		return recognize(0, tokens);
	}

	/**
	 * Recognizes a list of token.
	 * <p>
	 * If this method returns true, you can retrieve the position of the end of
	 * the recognized sequence, plus a list of markers indicating exactly what
	 * was recognized by the various elements.
	 *
	 * <p>
	 * About markers: if the language is L1,L2,...Ln,
	 * <p>
	 * the marker vector v1,...vn contains that ending position of the text
	 * recognized by language Li.
	 *
	 * @param startPos the first position to search. 0 for beginning of the list.
	 * @param tokens
	 * @return true iff a terminal state was reached <em>while</em> reading the list of tokens.
	 */
	public boolean recognize(int startPos, List<T> tokens) {
		markers = null;
		terminal = false;
		MarkedState currentState = new MarkedState(
				sequenceLanguage.getInitialStates(), startPos);
		// Now, read tokens until either the list has been seen or a terminal
		// position has been reached.
		for (int pos = startPos; pos < tokens.size() && !(terminal && earlyStop); pos++) {
			T token = tokens.get(pos);
			currentState = accept(currentState, token, pos + 1);
		}

		if (terminal) {
			// Find a candidate "best" state.
			SequenceLanguage<T>.SequenceState best = null;
			List<Integer> bestMark = null;
			for (Entry<SequenceLanguage<T>.SequenceState, ArrayList<Integer>> entry : currentState.markerMap
					.entrySet()) {
				if (entry.getKey().isTerminal()) {
					if (best == null
							|| isBetterMark(entry.getValue(), bestMark)) {
						best = entry.getKey();
						bestMark = entry.getValue();
					}
				}
			}
			markers = bestMark;
		}
		return terminal;
	}

	/**
	 * Accept a token, and returns the corresponding new state (with markers).
	 *
	 * @param current
	 * @param token
	 * @param pos
	 * @return
	 */
	private MarkedState accept(MarkedState current, T token, int pos) {
		MarkedState next = new MarkedState();
		for (SequenceLanguage<T>.SequenceState currentState : current.children) {
			ArrayList<Integer> currentStateMarker = current
					.getMarker(currentState);
			for (SequenceLanguage<T>.SequenceState nextState : currentState
					.accept(token)) {
				next.addState(currentState, nextState, currentStateMarker, pos);
			}
		}
		return next;
	}

	/**
	 * Returns the position of the end of the recognized text.
	 * <p>
	 * The system will always favour the first position.
	 *
	 * @return the ending position, or -1 if none.
	 */
	public int endingPosition() {
		if (markers == null) {
			return -1;
		} else {
			return markers.get(markers.size() - 1);
		}
	}

	/**
	 * Returns the list of <em>ending positions</em> for all recognized
	 * sub-languages.
	 * <p>
	 * Positions fall <em>between</em> token.
	 * </p>
	 * <p>
	 * Suppose the language is L1=(aa)* L2=bc*b L3=da,
	 * <p>
	 * if the recognized text is bcccbda, then we will have
	 * <p>
	 * [0, 5,7].
	 *
	 * @return
	 */
	public List<Integer> getMarkers() {
		if (markers != null) {
			return new ArrayList<>(markers);
		} else {
			return null;
		}
	}

	/**
	 * Compare two marker lists.
	 * <p>
	 * Basically, we favour the early match.
	 *
	 * @param marker1
	 * @param marker2
	 * @return
	 */
	private static boolean isBetterMark(List<Integer> marker1,
										List<Integer> marker2) {
		int l = Math.min(marker1.size(), marker2.size());
		int i = 0;
		while (i < l && marker1.get(i).equals(marker2.get(i))) {
			i++;
		}
		// We are at the first position where elements are different.
		if (i == l) {
			return (marker1.size() < marker2.size());
		} else {
			return marker1.get(i) < marker2.get(i);
		}
	}

	/**
	 * A state here is a couple of a simple set of children states and
	 * associated marker vectors.
	 *
	 * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
	 */
	private class MarkedState {
		// The markers for the states corresponding to the more recently read
		// tokens.

		public HashMap<SequenceLanguage<T>.SequenceState, ArrayList<Integer>> markerMap = new HashMap<>();
		public Set<SequenceLanguage<T>.SequenceState> children = new HashSet<>();

		/**
		 * Build a state for position 0.
		 *
		 * @param children
		 */
		public MarkedState(Set<SequenceLanguage<T>.SequenceState> children) {
			super();
			ArrayList<Integer> emptyList = new ArrayList<>();
			for (SequenceLanguage<T>.SequenceState child : children) {
				addState(null, child, emptyList, 0);
			}
		}

		/**
		 * Builds a state for an arbitrary initial position.
		 *
		 * @param children
		 * @param initialPosition the position at which we start to scan the text.
		 */
		public MarkedState(Set<SequenceLanguage<T>.SequenceState> children, int initialPosition) {
			super();
			ArrayList<Integer> emptyList = new ArrayList<>();
			for (SequenceLanguage<T>.SequenceState child : children) {
				addState(null, child, emptyList, initialPosition);
			}
		}

		/**
		 * Add a state for a new position.
		 * <p>
		 * post : the state is recorded, and its markers are known.
		 *
		 * <h3>Note on marker updating</h3>
		 * <p>
		 * Invariant : for a state s corresponding to childIndex k:
		 * <ul>
		 * <li>if s is not terminal for the sub language, the size of the marker
		 * will be k ;
		 * <li>if s is terminal for the sub language, the size of the marker
		 * will be k + 1.
		 * </ul>
		 * <p>
		 * Markers indicates the limits of the sub-languages in the sequence.
		 * Because of languages which recognize the zero-string, and due to the
		 * Sequence recognizer, we will sometime recognize more than one
		 * language with a token.
		 * <p>
		 * the algorithm will also place us, not in the final state of the
		 * recognized language, but in the initial state of the next language in
		 * the string.
		 * <p>
		 * For all those reasons, only looking at the current state is not an
		 * easy way to notice when markers should be set. The easiest way is to
		 * compare the previous one and the current one. If
		 * <ul>
		 * <li>the current state and the previous state correspond to different
		 * languages, the marker for the previous language should be updated (if
		 * it wasn't already correct)</li>
		 * <li>the current state is "child-terminal", then the marker for the
		 * current language should be updated</li>
		 * </ul>
		 *
		 * @param previousState   : the state before reaching this one (null if
		 *                        this is an initial state).
		 * @param childState      the state
		 * @param previousMarkers the marker, <em>before</em> reaching this
		 *                        state.
		 * @param pos             the position after the token which made this state
		 *                        reachable.
		 */
		private void addState(SequenceLanguage<T>.SequenceState previousState,
							  SequenceLanguage<T>.SequenceState childState,
							  ArrayList<Integer> previousMarkers, int pos) {
			children.add(childState);
			if (childState.isTerminal()) {
				terminal = true;
			}

			int previousLanguageIndex = -1;
			if (previousState != null) {
				previousLanguageIndex = previousState.getChildIndex();
			}

			// Update the marker map...
			ArrayList<Integer> nextStateMarker;
			int markerSize;

			if (previousLanguageIndex == childState.getChildIndex()
					&& !childState.isTerminalForSubLanguage()) {
				// We are still in the same language, hence the markers are the
				// same
				nextStateMarker = previousMarkers;

			} else {
				// Compute the size of the marker.
				if (childState.isTerminalForSubLanguage()) {
					markerSize = childState.getChildIndex() + 1;
				} else {
					markerSize = childState.getChildIndex();
				}
				nextStateMarker = new ArrayList<>(previousMarkers);
				// We might have recognized more than one language at
				// once (a language might recognize "").
				while (nextStateMarker.size() < markerSize) {
					nextStateMarker.add(pos);
				}
				// update the slot from previous language to current language if needed :
				if (previousLanguageIndex >= 0 && previousLanguageIndex < childState.getChildIndex()) {
					nextStateMarker.set(previousLanguageIndex, pos);
				}
				// If we are in a terminal state, update
				if (childState.isTerminalForSubLanguage()) {
					nextStateMarker.set(childState.getChildIndex(), pos);
				}
			}
			// update the map, iff the new marker is "better" than the
			// old one.
			if (!markerMap.containsKey(childState)
					|| isBetterMark(nextStateMarker, getMarker(childState))) {
				markerMap.put(childState, nextStateMarker);
			}
		}

		public MarkedState() {
			super();
		}

		/**
		 * Returns the marker currently associated with a state
		 *
		 * @param currentState
		 * @return
		 */
		public ArrayList<Integer> getMarker(
				SequenceLanguage<T>.SequenceState currentState) {
			return markerMap.get(currentState);
		}
	}

}
