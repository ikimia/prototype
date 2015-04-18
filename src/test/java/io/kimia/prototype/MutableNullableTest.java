package io.kimia.prototype;

/**
 * Tests mutable prototype outcome
 * (annotated with {@link io.kimia.prototype.Prototype} and {@link io.kimia.prototype.Mutable})
 * with a field annotated with {@link io.kimia.prototype.Nullable}.
 */
public class MutableNullableTest extends BasePrototypeTest<MutableNullableField> {

    @Mutable @Prototype class _MutableNullableField { @Nullable String s; }

    public MutableNullableTest() {
        super(MutableNullableField.class,
                new Class<?>[0],
                new String[] { "getS", "setS" });
    }

    @Override
    protected MutableNullableField generateRandomObject() {
        return newMutableNullableField(null);
    }

    @Override
    protected MutableNullableField generateObjectEqualTo(MutableNullableField mutableNullableField) {
        return newMutableNullableField(mutableNullableField.getS());
    }

    @Override
    protected String expectedToString(MutableNullableField mutableNullableField) {
        return "MutableNullableField{s=null}";
    }

    private static MutableNullableField newMutableNullableField(String s) {
        MutableNullableField mutableNullableField = new MutableNullableField();
        mutableNullableField.setS(s);
        return mutableNullableField;
    }
}
