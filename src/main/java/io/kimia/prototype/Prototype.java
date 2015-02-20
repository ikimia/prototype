package io.kimia.prototype;

import java.lang.annotation.*;

/**
 * <p>
 * Annotates a class as a prototype of another auto-generated class by the Prototype framework.
 * </p>
 *
 * <p>
 * The name of the classes that are annotated with this annotation must start with an underscore character (_).
 * </p>
 *
 * <p>
 * The auto-generated classes are described as following:
 * </p>
 * <ol>
 *     <li>They will be produced in the same package as the prototype.</li>
 *     <li>They will be declared {@code public final}.</li>
 *     <li>Their name will be the same as the prototype's without the leading underscore.</li>
 *     <li>
 *         They will have members of the same types and names as the prototype,
 *         all {@code private final} except for those annotated with
 *         {@link io.kimia.prototype.Mutable} (which are just private).
 *     </li>
 *     <li>
 *         A constructor with all the immutable members as parameters will be created.
 *         The order of the parameters will be the same as their appearance in the prototype.
 *     </li>
 *     <li>Getters (and setters for mutable fields) will be created.</li>
 *     <li>{@code toString}, {@code equals} and {@code hashCode} will be created according to the members.</li>
 *     <li>
 *         All members that might be null, will be validated as not null when assigned,
 *         unless they are annotated with {@link io.kimia.prototype.Nullable}.
 *     </li>
 * </ol>
 *
 * <p>
 * For example:
 * </p>
 * <pre>
 * package com.example;
 *
 * &#064;Prototype class _Pet {
 *     String name;
 *     &#064;Mutable int age;
 *     &#064;Nullable String voice;
 * }
 * </pre>
 *
 * <p>
 * Will produce:
 * </p>
 * <pre>
 * package com.example;
 *
 * public final Pet {
 *     private final String name;
 *     private int age;
 *     private final String voice;
 *
 *     public Pet(String name, String voice) {...}
 *     public String getName() {...}
 *     public int getAge() {...}
 *     public void setAge(int age) {...}
 *     public String getVoice() {...}
 *     &#064;Override public int hashCode() {...}
 *     &#064;Override public boolean equals(Object o) {...}
 *     &#064;Override public String toString() {...}
 * }
 * </pre>
 *
 * @see io.kimia.prototype.Mutable
 * @see io.kimia.prototype.Nullable
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface Prototype {
}
