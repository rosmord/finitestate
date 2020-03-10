package org.qenherkhopeshef.finiteState.lazy;

import java.util.Set;

/**
 * A label recognising any element not in a set.
 * @author rosmord
 * @param <T> 
 */
public class ComplementSetLabel<T> implements LazyLabelIF<T>{

    private Set<T> impossibleTokens;

    public ComplementSetLabel(Set<T> impossibleTokens) {
        this.impossibleTokens = impossibleTokens;
    }
    
    
    @Override
    public boolean matches(T token) {
        return ! impossibleTokens.contains(token);
    }
    
}
