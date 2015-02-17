package io.kimia.prototype;

import java.lang.annotation.*;

/**
 * <p>
 * Annotates a prototype class member to produce a mutable member in the concrete class.
 * </p>
 *
 * <p>
 * Generated members as a result of this annotation will be declared {@code private}
 * instead of {@code private final} and setters will be created for them.
 * </p>
 *
 * @see Prototype
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.FIELD})
@Documented
public @interface Mutable {
    boolean value() default true;
}
