package org.qenherkhopeshef.finitestate.lazy;

/**
 * Labels which can match Lazy data.
 * <p>In complex cases where you need to match properties of objects, this is the class you might need to implement.</p>
 * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
 * @param <T> the token type.
 *
 */
public interface LazyLabelIF<T> {

    /**
     * @param token
     * @return
     */
	boolean matches(T token);
}
