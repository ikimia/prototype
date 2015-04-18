package io.kimia.prototype;

import java.util.Random;

/**
 * Tests a prototype outcome's basic functionality with all java primitives.
 * This test is important especially for {@link Object#hashCode()} implementation because it requires
 * converting all types to {@code int}.
 */
public class PrimitivesPrototypeTest extends BasePrototypeTest<AllPrimitives> {

    @Prototype class _AllPrimitives {
        byte b;
        char c;
        short s;
        int i;
        float f;
        long l;
        double d;
        boolean bool;
    }

    private final Random random = new Random();

    public PrimitivesPrototypeTest() {
        super(AllPrimitives.class,
                new Class<?>[] {
                        byte.class, char.class, short.class, int.class,
                        float.class, long.class, double.class, boolean.class
                },
                new String[] { "getB", "getC", "getS", "getI", "getF", "getL", "getD", "isBool" });
    }

    @Override
    protected AllPrimitives generateRandomObject() {
        return new AllPrimitives(
                (byte) random.nextInt(),
                (char) random.nextInt(),
                (short) random.nextInt(),
                random.nextInt(),
                random.nextFloat(),
                random.nextLong(),
                random.nextDouble(),
                random.nextBoolean()
        );
    }

    @Override
    protected AllPrimitives generateObjectEqualTo(AllPrimitives allPrimitives) {
        return new AllPrimitives(
                allPrimitives.getB(),
                allPrimitives.getC(),
                allPrimitives.getS(),
                allPrimitives.getI(),
                allPrimitives.getF(),
                allPrimitives.getL(),
                allPrimitives.getD(),
                allPrimitives.isBool()
        );
    }

    @Override
    protected String expectedToString(AllPrimitives allPrimitives) {
        return "AllPrimitives{b="
                + allPrimitives.getB() + ", c="
                + allPrimitives.getC() + ", s="
                + allPrimitives.getS() + ", i="
                + allPrimitives.getI() + ", f="
                + allPrimitives.getF() + ", l="
                + allPrimitives.getL() + ", d="
                + allPrimitives.getD() + ", bool="
                + allPrimitives.isBool() + "}";
    }
}
