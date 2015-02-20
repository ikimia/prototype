package io.kimia.prototype;

import java.lang.annotation.*;

/**
 * <p>
 * Annotates a prototype definition to produce all members in the concrete class mutable
 * or a prototype member to produce a mutable member in the concrete class.
 * </p>
 *
 * <p>
 * Generated members as a result of this annotation will be declared {@code private}
 * instead of {@code private final} and setters will be created for them. In addition, mutable
 * members are not added to the generated constructor.
 * </p>
 *
 * <p>
 * Type mutability can be reverted for a specific field by annotating it as {@code &#064;Mutable(false)}.
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
