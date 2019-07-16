package org.qenherkhopeshef.finiteState.lazy;

import java.util.Set;

/**
 * Base interface for states created when parsing a text with a language.
 * <p> In a first version, the MultiState accept() method sent back another MetaState.
 * This can be correct, but the problem is then that those states can't be easily mixed (we could have ended with two
 * metastate with
 * the same information partly shared (find concrete examples, using the kleene star for instance - maybe something like
 * <code>((ab)*(ab)*))</code>.
 *
 * <p>Returning a set of states avoids the need to provide something like a "merge" operation.</p>
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
