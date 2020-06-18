/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.qenherkhopeshef.finitestate.lazy;

/**
 *
 * The complementation of a label.
 * @author rosmord
 */
public class ComplementLabel<T> implements LazyLabelIF<T>{

    private LazyLabelIF<T> complementedLabel;

    public ComplementLabel(LazyLabelIF<T> complementedLabel) {
        this.complementedLabel = complementedLabel;
    }
    
    /**
     * Matches token iff complementedLabel doesn't match token.
     * @param token
     * @return 
     */    
    public boolean matches(T token) {
        return ! complementedLabel.matches(token);
    }
    
}
