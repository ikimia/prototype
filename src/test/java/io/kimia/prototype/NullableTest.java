package io.kimia.prototype;

/**
 * Tests a prototype outcome with fields annotated with {@link io.kimia.prototype.Nullable}.
 */
public class NullableTest extends BasePrototypeTest<NullableField> {

    @Prototype class _NullableField {
        @Nullable String s;
    }

    public NullableTest() {
        super(NullableField.class,
                new Class<?>[] { String.class },
                new String[] { "getS" });
    }

    @Override
    protected NullableField generateRandomObject() {
        return new NullableField(null);
    }

    @Override
    protected NullableField generateObjectEqualTo(NullableField nullableField) {
        return new NullableField(nullableField.getS());
    }

    @Override
    protected String expectedToString(NullableField nullableField) {
        return "NullableField{s=null}";
    }
}
