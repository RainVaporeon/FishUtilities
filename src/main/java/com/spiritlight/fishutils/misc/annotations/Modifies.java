package com.spiritlight.fishutils.misc.annotations;

import java.lang.annotation.*;

/**
 * Annotation denoting that this method may be modifying the given
 * parameters. Alternatively, {@link Modifies#NONE} may be used
 * to indicate that no parameters are modified when passing into
 * the annotated method.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Documented
public @interface Modifies {
    String NONE = "";

    String[] value() default NONE;
}
