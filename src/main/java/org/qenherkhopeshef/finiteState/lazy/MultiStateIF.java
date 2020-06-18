package org.qenherkhopeshef.finitestate.lazy;

import java.util.Set;

/**
 * Base interface for states created when parsing a text with a language.
 * <p> In non-deterministic automata, a given input might lead to a number 
 * of different states. To make it deterministic, the principle is to replace 
 * this plurality of states by a set of states, which is the set of all set reachable
 * with a given input. And <em>this</em> is deterministic.</p>
 * <p> A MultiStateIF represents a state reachable when recognising a language part. When one
 * is in a given MultiStateIF, reading a new token might lead toward a <em>set</em> of other MultiStateIF.
 * 
 * <p> An important feature of MultiStates is that they must be usable in sets. Hence,
 * their equals and hashCode methods must be reasonable. 
 * </p>
 * 
 * @author rosmord
 * @param <T> the token type.
 */
interface MultiStateIF<T> {

    /**
     * Is this state a terminal state for its parent language ?
     *
     * @return
     */
    boolean isTerminal();

    /**
     * Where do we go if we read label ?
     *
     * @param token
     * @return
     */
    Set<? extends MultiStateIF<T>> accept(T token);

    /**
     * Get the parent automaton for this state.
     *
     * @return the language which has created this state.
     */
    RegularLanguageIF<T> getParent();
}
