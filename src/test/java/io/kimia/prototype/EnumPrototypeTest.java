package io.kimia.prototype;

import java.util.Random;

/**
 * Tests plain prototype outcome with enum fields.
 */
public class EnumPrototypeTest extends BasePrototypeTest<TestCard> {

    enum Suit { CLUB, DIAMOND, HEART, SPADE }
    enum Value { TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE }

    @Prototype class _TestCard {
        Suit suit;
        Value value;
    }

    private final Random random = new Random();

    public EnumPrototypeTest() {
        super(TestCard.class,
                new Class<?>[] { Suit.class, Value.class },
                new String[] { "getSuit", "getValue", "equals", "hashCode", "toString" });
    }

    @Override
    protected TestCard generateRandomObject() {
        Suit[] suits = Suit.values();
        Value[] values = Value.values();
        return new TestCard(suits[random.nextInt(suits.length)], values[random.nextInt(values.length)]);
    }

    @Override
    protected TestCard generateObjectEqualTo(TestCard testCard) {
        return new TestCard(testCard.getSuit(), testCard.getValue());
    }

    @Override
    protected String expectedToString(TestCard testCard) {
        return "TestCard{suit=" + testCard.getSuit() + ", value=" + testCard.getValue() + "}";
    }
}
