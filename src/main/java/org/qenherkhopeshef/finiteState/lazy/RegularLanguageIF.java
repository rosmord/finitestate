package org.qenherkhopeshef.finiteState.lazy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A language based on an alphabet of <em>tokens</em>, all of the same type.
 *
 * <p> To build instances from this class, look at {@link RegularLanguageFactory}
 *
 * @author rosmord
 * @param <T> the base type for the tokens.
 */
public interface RegularLanguageIF<T> {

    Set<? extends MultiStateIF<T>> getInitialStates();

    /**
     * Try to recognize a list of tokens.
     *
     * @param tokens
     * @return true, if the whole list of tokens belongs to the language.
     */
    default boolean recognize(List<T> tokens) {
        Set<? extends MultiStateIF<T>> currentState
                = getInitialStates();
        boolean terminal = false;
        for (T token : tokens) {
            Set<MultiStateIF<T>> nextState = new HashSet<>();
            for (MultiStateIF<T> state : currentState) {
                nextState.addAll(state.accept(token));
            }
            currentState = nextState;
        }
        for (MultiStateIF<T> state : currentState) {
            if (state.isTerminal()) {
                terminal = true;
                break;
            }
        }
        return terminal;
    }
}
