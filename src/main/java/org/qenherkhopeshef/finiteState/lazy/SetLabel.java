package org.qenherkhopeshef.finiteState.lazy;

import java.util.Set;

/**
 * A label recognising any element in a set.
 * @author rosmord
 * @param <T> 
 */
public class SetLabel<T> implements LazyLabelIF<T>{

    private Set<T> possibleTokens;

    public SetLabel(Set<T> possibleTokens) {
        this.possibleTokens = possibleTokens;
    }
    
    
    @Override
    public boolean matches(T token) {
        return possibleTokens.contains(token);
    }
    
}
