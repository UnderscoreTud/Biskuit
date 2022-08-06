package me.tud.biskuit.elements.expressions.arithmetic;

import ch.njol.skript.expressions.arithmetic.ArithmeticGettable;
import ch.njol.skript.expressions.arithmetic.NumberExpressionInfo;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.util.Utils;
import ch.njol.util.Checker;
import org.bukkit.event.Event;

import java.util.List;

public class BitwiseArithmeticChain implements ArithmeticGettable {

    @SuppressWarnings("unchecked")
    private static final Checker<Object> CHECKER =
            o -> o.equals(BitwiseOperator.AND) || o.equals(BitwiseOperator.OR)
                    || o.equals(BitwiseOperator.XOR) || o.equals(BitwiseOperator.SIGNED_RIGHT_SHIFT)
                    || o.equals(BitwiseOperator.UNSIGNED_RIGHT_SHIFT) || o.equals(BitwiseOperator.LEFT_SHIFT);

    private final ArithmeticGettable left;
    private final BitwiseOperator operator;
    private final ArithmeticGettable right;

    public BitwiseArithmeticChain(ArithmeticGettable left, BitwiseOperator operator, ArithmeticGettable right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Number get(Event event, boolean integer) {
        return operator.calculate(left.get(event, true), right.get(event, true));
    }

    @SuppressWarnings("unchecked")
    public static ArithmeticGettable parse(List<Object> chain) {
        int lastIndex = Utils.findLastIndex(chain, CHECKER);

        if (lastIndex != -1) {
            List<Object> leftChain = chain.subList(0, lastIndex);
            ArithmeticGettable left = parse(leftChain);

            BitwiseOperator operator = (BitwiseOperator) chain.get(lastIndex);

            List<Object> rightChain = chain.subList(lastIndex + 1, chain.size());
            ArithmeticGettable right = parse(rightChain);

            return new BitwiseArithmeticChain(left, operator, right);
        }

        if (chain.size() != 1)
            throw new IllegalStateException();

        return new NumberExpressionInfo((Expression<? extends Number>) chain.get(0));
    }

}
