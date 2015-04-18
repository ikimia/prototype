package io.kimia.prototype;

import java.util.Random;

/**
 * Tests partially mutable prototype outcome.
 */
public class MixedMutableTest extends BasePrototypeTest<MixedMutable> {

    @Prototype class _MixedMutable {
        @Mutable String string;
        Number number;
        boolean bool;
    }

    private final Random random = new Random();

    public MixedMutableTest() {
        super(MixedMutable.class,
                new Class<?>[] { Number.class, boolean.class },
                new String[] { "getString", "setString", "getNumber", "isBool" });
    }

    @Override
    protected MixedMutable generateRandomObject() {
        return newMixedMutable(String.valueOf(random.nextInt()), random.nextDouble(), random.nextBoolean());
    }

    @Override
    protected MixedMutable generateObjectEqualTo(MixedMutable mixedMutable) {
        return newMixedMutable(mixedMutable.getString(), mixedMutable.getNumber(), mixedMutable.isBool());
    }

    @Override
    protected String expectedToString(MixedMutable mixedMutable) {
        return "MixedMutable{string=" + mixedMutable.getString()
                + ", number=" + mixedMutable.getNumber()
                + ", bool=" + mixedMutable.isBool() + "}";
    }

    private static MixedMutable newMixedMutable(String string, Number number, boolean bool) {
        MixedMutable mixedMutable = new MixedMutable(number, bool);
        mixedMutable.setString(string);
        return mixedMutable;
    }
}
