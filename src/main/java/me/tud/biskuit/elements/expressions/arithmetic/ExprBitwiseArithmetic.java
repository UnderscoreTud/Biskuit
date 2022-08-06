package me.tud.biskuit.elements.expressions.arithmetic;

import ch.njol.skript.Skript;
import ch.njol.skript.expressions.arithmetic.ArithmeticGettable;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Patterns;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ExprBitwiseArithmetic extends SimpleExpression<Number> {

    private static final Class<?>[] INTEGER_CLASSES = {Long.class, Integer.class, Short.class, Byte.class};

    private record PatternInfo(BitwiseOperator operator, boolean leftGrouped, boolean rightGrouped) {}

    private final static Patterns<PatternInfo> patterns = new Patterns<>(new Object[][]{

            {"\\(%number%\\)[ ]&[ ]\\(%number%\\)", new PatternInfo(BitwiseOperator.AND, true, true)},
            {"\\(%number%\\)[ ]&[ ]%number%", new PatternInfo(BitwiseOperator.AND, true, false)},
            {"%number%[ ]&[ ]\\(%number%\\)", new PatternInfo(BitwiseOperator.AND, false, true)},
            {"%number%[ ]&[ ]%number%", new PatternInfo(BitwiseOperator.AND, false, false)},

            {"\\(%number%\\)[ ]\\|[ ]\\(%number%\\)", new PatternInfo(BitwiseOperator.OR, true, true)},
            {"\\(%number%\\)[ ]\\|[ ]%number%", new PatternInfo(BitwiseOperator.OR, true, false)},
            {"%number%[ ]\\|[ ]\\(%number%\\)", new PatternInfo(BitwiseOperator.OR, false, true)},
            {"%number%[ ]\\|[ ]%number%", new PatternInfo(BitwiseOperator.OR, false, false)},

            {"\\(%number%\\)[ ]xor[ ]\\(%number%\\)", new PatternInfo(BitwiseOperator.XOR, true, true)},
            {"\\(%number%\\)[ ]xor[ ]%number%", new PatternInfo(BitwiseOperator.XOR, true, false)},
            {"%number%[ ]xor[ ]\\(%number%\\)", new PatternInfo(BitwiseOperator.XOR, false, true)},
            {"%number%[ ]xor[ ]%number%", new PatternInfo(BitwiseOperator.XOR, false, false)},

            {"\\(%number%\\)[ ]\\>\\>[ ]\\(%number%\\)", new PatternInfo(BitwiseOperator.SIGNED_RIGHT_SHIFT, true, true)},
            {"\\(%number%\\)[ ]\\>\\>[ ]%number%", new PatternInfo(BitwiseOperator.SIGNED_RIGHT_SHIFT, true, false)},
            {"%number%[ ]\\>\\>[ ]\\(%number%\\)", new PatternInfo(BitwiseOperator.SIGNED_RIGHT_SHIFT, false, true)},
            {"%number%[ ]\\>\\>[ ]%number%", new PatternInfo(BitwiseOperator.SIGNED_RIGHT_SHIFT, false, false)},

            {"\\(%number%\\)[ ]\\>\\>\\>[ ]\\(%number%\\)", new PatternInfo(BitwiseOperator.UNSIGNED_RIGHT_SHIFT, true, true)},
            {"\\(%number%\\)[ ]\\>\\>\\>[ ]%number%", new PatternInfo(BitwiseOperator.UNSIGNED_RIGHT_SHIFT, true, false)},
            {"%number%[ ]\\>\\>\\>[ ]\\(%number%\\)", new PatternInfo(BitwiseOperator.UNSIGNED_RIGHT_SHIFT, false, true)},
            {"%number%[ ]\\>\\>\\>[ ]%number%", new PatternInfo(BitwiseOperator.UNSIGNED_RIGHT_SHIFT, false, false)},

            {"\\(%number%\\)[ ]\\<\\<[ ]\\(%number%\\)", new PatternInfo(BitwiseOperator.LEFT_SHIFT, true, true)},
            {"\\(%number%\\)[ ]\\<\\<[ ]%number%", new PatternInfo(BitwiseOperator.LEFT_SHIFT, true, false)},
            {"%number%[ ]\\<\\<[ ]\\(%number%\\)", new PatternInfo(BitwiseOperator.LEFT_SHIFT, false, true)},
            {"%number%[ ]\\<\\<[ ]%number%", new PatternInfo(BitwiseOperator.LEFT_SHIFT, false, false)},
    });

    static {
        Skript.registerExpression(ExprBitwiseArithmetic.class, Number.class, ExpressionType.PATTERN_MATCHES_EVERYTHING, patterns.getPatterns());
    }

    private Expression<? extends Number> first;
    private Expression<? extends Number> second;
    private BitwiseOperator operator;
    private final List<Object> chain = new ArrayList<>();
    private ArithmeticGettable arithmeticGettable;

    @Override
    protected Number[] get(Event e) {
        Number[] number = (Number[]) Array.newInstance(Long.class, 1);
        number[0] = arithmeticGettable.get(e, true);
        return number;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return first.toString(e, debug) + " " + operator + " " + second.toString(e, debug);
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        first = (Expression<? extends Number>) exprs[0];
        second = (Expression<? extends Number>) exprs[1];
        PatternInfo patternInfo = patterns.getInfo(matchedPattern);
        operator = patternInfo.operator;
        if (first instanceof ExprBitwiseArithmetic arithmetic && !patternInfo.leftGrouped)
            chain.addAll(arithmetic.chain);
        else chain.add(first);
        chain.add(operator);
        if (second instanceof ExprBitwiseArithmetic arithmetic && !patternInfo.leftGrouped)
            chain.addAll(arithmetic.chain);
        else chain.add(second);
        arithmeticGettable = BitwiseArithmeticChain.parse(chain);
        return true;
    }
}
