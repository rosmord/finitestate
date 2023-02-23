package org.qenherkhopeshef.finitestate.lazy;

import java.util.Collections;
import java.util.Set;

/**
 * The emptySequence string language, which recognizes a emptySequence list of tokens.
 * Do not confuse with the "emptySequence language", which recognizes <em>nothing</em>.
 * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
 * @param <T> token type.
 */
class EmptyStringLanguage<T> implements RegularLanguageIF<T> {

    private final Set<? extends MultiStateIF<T>> firstStateSet;
    private final Set<? extends MultiStateIF<T>> secondStateSet;

    public EmptyStringLanguage() {
        firstStateSet = Collections.singleton(new FirstState());
        secondStateSet = Collections.singleton(new SecondState());
    }

    @Override
    public Set<? extends MultiStateIF<T>> getInitialStates() {
        return firstStateSet;
    }

    private class FirstState implements MultiStateIF<T> {

        @Override
        public boolean isTerminal() {
            return true;
        }

        @Override
        public Set<? extends MultiStateIF<T>> accept(T token) {
            return secondStateSet;
        }

        @Override
        public RegularLanguageIF<T> getParent() {
            return EmptyStringLanguage.this;
        }

        @Override
        public String toString() {
            return "(0 ok)";
        }

    }

    private class SecondState implements MultiStateIF<T> {

        @Override
        public boolean isTerminal() {
            return false;
        }

        @Override
        public Set<? extends MultiStateIF<T>> accept(T token) {
            return Collections.emptySet();
        }

        @Override
        public RegularLanguageIF<T> getParent() {
            return EmptyStringLanguage.this;
        }

        @Override
        public String toString() {
            return "(0 no)";
        }
    }

    @Override
    public String toString() {
        return "[L ø]";
    }
}
