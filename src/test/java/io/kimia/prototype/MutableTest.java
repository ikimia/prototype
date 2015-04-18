package io.kimia.prototype;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import java.util.Random;

/**
 * Tests mutable prototype outcome
 * (annotated with {@link io.kimia.prototype.Prototype} and {@link io.kimia.prototype.Mutable}).
 */
public class MutableTest extends BasePrototypeTest<MutableTestPoint> {

    @Mutable @Prototype class _MutableTestPoint { int x, y; }

    private final Random random = new Random();

    public MutableTest() {
        super(MutableTestPoint.class,
                new Class<?>[0],
                new String[] { "getX", "getY", "setX", "setY" });
    }

    @Test
    public void testSettersAndGettersFunctionality() {
        int x = random.nextInt();
        int y = random.nextInt();
        MutableTestPoint point = newPoint(x, y);
        assertEquals(point.getX(), x);
        assertEquals(point.getY(), y);
    }

    @Override
    protected MutableTestPoint generateRandomObject() {
        return newPoint(random.nextInt(), random.nextInt());
    }

    @Override
    protected MutableTestPoint generateObjectEqualTo(MutableTestPoint mutableTestPoint) {
        return newPoint(mutableTestPoint.getX(), mutableTestPoint.getY());
    }

    @Override
    protected String expectedToString(MutableTestPoint mutableTestPoint) {
        return "MutableTestPoint{x=" + mutableTestPoint.getX() + ", y=" + mutableTestPoint.getY() + "}";
    }

    private static MutableTestPoint newPoint(int x, int y) {
        MutableTestPoint point = new MutableTestPoint();
        point.setX(x);
        point.setY(y);
        return point;
    }
}
