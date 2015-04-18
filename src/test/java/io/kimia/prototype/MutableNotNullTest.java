package io.kimia.prototype;

import org.testng.annotations.Test;

import java.util.Random;

/**
 * Tests mutable prototype outcome
 * (annotated with {@link io.kimia.prototype.Prototype} and {@link io.kimia.prototype.Mutable})
 * that does not accept {@code null} as value for a field by default.
 */
public class MutableNotNullTest extends BasePrototypeTest<MutableNotNullField> {

    @Mutable @Prototype class _MutableNotNullField { String s; }

    private final Random random = new Random();

    public MutableNotNullTest() {
        super(MutableNotNullField.class,
                new Class<?>[0],
                new String[] { "getS", "setS" });
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testSetNullField() {
        newMutableNotNullField(null);
    }

    @Override
    protected MutableNotNullField generateRandomObject() {
        return newMutableNotNullField(String.valueOf(random.nextInt()));
    }

    @Override
    protected MutableNotNullField generateObjectEqualTo(MutableNotNullField mutableNotNullField) {
        return newMutableNotNullField(mutableNotNullField.getS());
    }

    @Override
    protected String expectedToString(MutableNotNullField mutableNotNullField) {
        return "MutableNotNullField{s=" + mutableNotNullField.getS() + "}";
    }

    private static MutableNotNullField newMutableNotNullField(String s) {
        MutableNotNullField mutableNotNullField = new MutableNotNullField();
        mutableNotNullField.setS(s);
        return mutableNotNullField;
    }

}
