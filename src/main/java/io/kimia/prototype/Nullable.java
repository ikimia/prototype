package io.kimia.prototype;

import java.lang.annotation.*;

/**
 * <p>
 * Annotates a prototype class member to produce a nullable member in the concrete class.
 * </p>
 *
 * <p>
 * Generated members as a result of this annotation will not be validated as not null when assigned.
 * Instead, a null check will be preformed every time they are referenced.
 * </p>
 *
 * @see Prototype
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
@Documented
public @interface Nullable {
}
