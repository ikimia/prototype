package io.kimia.prototype;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * Object methods contracts to be tested on all prototype outcomes.
 */
public abstract class BasePrototypeTest<T> {
    private final Class<T> tType;
    private final Class<?>[] expectedConstructorParameterTypes;
    private final String[] expectedDeclaredMethods;

    protected BasePrototypeTest(
            Class<T> tType, Class<?>[] expectedConstructorParameterTypes, String[] expectedDeclaredMethods) {

        this.tType = tType;
        this.expectedConstructorParameterTypes = expectedConstructorParameterTypes;
        this.expectedDeclaredMethods = expectedDeclaredMethods;
    }

    @Test
    public void testSinglePublicConstructor() {
        Constructor<?>[] constructors = tType.getConstructors();
        assertEquals(constructors.length, 1);
        assertEquals(constructors[0].getModifiers(), Modifier.PUBLIC);
        assertEquals(constructors[0].getParameterTypes(), expectedConstructorParameterTypes);
    }

    @Test
    public void testDeclaredMethods() {
        Method[] methods = tType.getDeclaredMethods();
        String[] methodNames = new String[methods.length];
        for (int i = 0; i < methods.length; i++) {
            methodNames[i] = methods[i].getName();
        }
        Arrays.sort(methodNames);
        Arrays.sort(expectedDeclaredMethods);
        assertEquals(methodNames, expectedDeclaredMethods, Arrays.toString(methodNames));
    }

    @Test
    public void testEqualsFunctionality() {
        T t = generateRandomObject();
        T equalToT = generateObjectEqualTo(t);
        T anotherEqualToT = generateObjectEqualTo(equalToT);

        // Preconditions
        assertNotSame(t, equalToT);
        assertNotSame(t, anotherEqualToT);
        assertNotSame(equalToT, anotherEqualToT);

        // Reflexive
        assertEquals(t, t);

        // Symmetric
        assertEquals(t, equalToT);
        assertEquals(equalToT, t);

        // Transitive
        assertEquals(equalToT, anotherEqualToT);
        assertEquals(t, anotherEqualToT);

        // Consistent
        assertEquals(t, equalToT);

        // Return false for x.equals(null)
        assertNotEquals(t, null);
    }

    @Test(dependsOnMethods = "testEqualsFunctionality")
    public void testHashCodeFunctionality() {
        T t = generateRandomObject();
        T equalToT = generateObjectEqualTo(t);

        // Consistent
        assertEquals(t.hashCode(), t.hashCode());

        // x.equals(y) => x.hashCode() == y.hashCode()
        assertEquals(t.hashCode(), equalToT.hashCode());
    }

    @Test
    public void testToString() {
        T t = generateRandomObject();
        assertEquals(t.toString(), expectedToString(t));
    }

    protected abstract T generateRandomObject();
    protected abstract T generateObjectEqualTo(T t);
    protected abstract String expectedToString(T t);
}
