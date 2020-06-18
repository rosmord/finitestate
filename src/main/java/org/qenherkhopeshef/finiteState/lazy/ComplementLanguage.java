package org.qenherkhopeshef.finitestate.lazy;

import java.util.HashSet;
import java.util.Set;

/**
 * The complement of a given language.
 * <p>
 * Remainder : if the language l does not recognize the emptySequence string, then
 * complement(l) recognizes it.
 *
 * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
 *
 * @param <T> token type.
 */
class ComplementLanguage<T> implements
        RegularLanguageIF<T> {

    private final RegularLanguageIF<T> complemented;

    public ComplementLanguage(RegularLanguageIF<T> negated) {
        super();
        this.complemented = negated;
    }

    @Override
    public Set<? extends MultiStateIF<T>> getInitialStates() {
        HashSet<ComplementState> result = new HashSet<>();
        Set<? extends MultiStateIF<T>> newChildren = complemented.getInitialStates();
        result.add(new ComplementState(newChildren));
        return result;
    }

    /**
     * A possible state while accepting a word in a complement language.
     * <p>
     * When a token is accepted, the resulting state is terminal iff accepting
     * the token in the complemented language does <b>not</b> lead to a terminal
     * state.
     *
     * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
     */
    private class ComplementState extends AbstractMultiState<T> {

        private final Set<? extends MultiStateIF<T>> complementedStates;

        public ComplementState(
                Set<? extends MultiStateIF<T>> complementedStates) {
            super(!containsTerminal(complementedStates));
            this.complementedStates = complementedStates;
        }

        @Override
        public Set<ComplementState> accept(T token) {
            HashSet<ComplementState> result = new HashSet<>();
            HashSet<MultiStateIF<T>> newChildren = new HashSet<>();
            for (MultiStateIF<T> childState : complementedStates) {
                newChildren.addAll(childState.accept(token));
            }
            result.add(new ComplementState(newChildren));
            return result;
        }

        @Override
        public RegularLanguageIF<T> getParent() {
            return ComplementLanguage.this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ComplementLanguage.ComplementState) {
                ComplementState other = (ComplementState) obj;
                return this.getParent().equals(other.getParent())
                        && this.terminal == other.terminal
                        && // This one is
                        // normally not
                        // needed.
                        this.complementedStates
                                .equals(other.complementedStates);
            } else {
                return false;
            }

        }

        @Override
        public int hashCode() {
            return getParent().hashCode() + 31 * complementedStates.hashCode();
        }

        @Override
        public String toString() {
            return "(~" + complementedStates + ")";
        }

    }

    @Override
    public String toString() {
        return "[~ " + complemented + "]";
    }

    /**
     * Auxiliary method to see if one state in a set at least is terminal.
     *
     * @param states
     * @return
     */
    private static boolean containsTerminal(
            Set<? extends MultiStateIF<?>> states) {
        boolean hasTerminal = false;
        for (MultiStateIF<?> newChild : states) {
            if (newChild.isTerminal()) {
                hasTerminal = true;
                break;
            }
        }
        return hasTerminal;
    }
}
