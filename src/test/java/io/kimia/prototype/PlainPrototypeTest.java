package io.kimia.prototype;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import java.util.Random;

/**
 * Tests plain prototype outcome (no other annotations but {@link io.kimia.prototype.Prototype}).
 */
public class PlainPrototypeTest extends BasePrototypeTest<TestPoint> {

    @Prototype class _TestPoint { int x, y; }

    private final Random random = new Random();

    public PlainPrototypeTest() {
        super(TestPoint.class,
                new Class<?>[] { int.class, int.class },
                new String[] { "getX", "getY", "equals", "hashCode", "toString" });
    }

    @Test
    public void testGettersFunctionality() {
        int x = random.nextInt();
        int y = random.nextInt();
        TestPoint point = new TestPoint(x, y);
        assertEquals(point.getX(), x);
        assertEquals(point.getY(), y);
    }

    @Override
    protected TestPoint generateRandomObject() {
        return new TestPoint(random.nextInt(), random.nextInt());
    }

    @Override
    protected TestPoint generateObjectEqualTo(TestPoint testPoint) {
        return new TestPoint(testPoint.getX(), testPoint.getY());
    }

    @Override
    protected String expectedToString(TestPoint testPoint) {
        return "TestPoint{x=" + testPoint.getX() + ", y=" + testPoint.getY() + "}";
    }
}
