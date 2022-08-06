package me.tud.biskuit.elements.expressions.arithmetic;

public enum BitwiseOperator {

    AND("&") {
        @Override
        @SuppressWarnings("null")
        public Number calculate(Number n1, Number n2) {
            return n1.longValue() & n2.longValue();
        }
    },
    OR("|") {
        @Override
        @SuppressWarnings("null")
        public Number calculate(Number n1, Number n2) {
            return n1.longValue() | n2.longValue();
        }
    },
    XOR("^") {
        @Override
        @SuppressWarnings("null")
        public Number calculate(Number n1, Number n2) {
            return n1.longValue() ^ n2.longValue();
        }
    },
    SIGNED_RIGHT_SHIFT(">>") {
        @Override
        @SuppressWarnings("null")
        public Number calculate(Number n1, Number n2) {
            return n1.longValue() >> n2.longValue();
        }
    },
    UNSIGNED_RIGHT_SHIFT(">>>") {
        @Override
        @SuppressWarnings("null")
        public Number calculate(Number n1, Number n2) {
            return n1.longValue() >>> n2.longValue();
        }
    },
    LEFT_SHIFT("<<") {
        @Override
        @SuppressWarnings("null")
        public Number calculate(Number n1, Number n2) {
            return n1.longValue() << n2.longValue();
        }
    };

    private final String sign;

    BitwiseOperator(String sign) {
        this.sign = sign;
    }

    public abstract Number calculate(Number n1, Number n2);

    @Override
    public String toString() {
        return sign;
    }

}
