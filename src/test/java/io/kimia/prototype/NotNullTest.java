package io.kimia.prototype;

import org.testng.annotations.Test;

import java.util.Random;

/**
 * Tests a regular prototype outcome that does not accept {@code null} as value for a field by default.
 */
public class NotNullTest extends BasePrototypeTest<NotNullField> {

    @Prototype class _NotNullField { String s; }

    private final Random random = new Random();

    public NotNullTest() {
        super(NotNullField.class,
                new Class<?>[] { String.class },
                new String[] { "getS", "equals", "hashCode", "toString" });
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testNullFieldInConstructor() {
        new NotNullField(null);
    }

    @Override
    protected NotNullField generateRandomObject() {
        return new NotNullField(String.valueOf(random.nextInt()));
    }

    @Override
    protected NotNullField generateObjectEqualTo(NotNullField notNullField) {
        return new NotNullField(notNullField.getS());
    }

    @Override
    protected String expectedToString(NotNullField notNullField) {
        return "NotNullField{s=" + notNullField.getS() + "}";
    }
}
