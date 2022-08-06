package me.tud.biskuit.elements.expressions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ExprBits extends PropertyExpression<Number, Object> {

    static {
        register(ExprBits.class, Object.class, "bit (:array|string)", "numbers");
    }

    boolean isArray = false;

    @Override
    protected Object[] get(Event e, Number[] source) {
        if (isArray) {
            List<Long> bits = new ArrayList<>();
            for (Number number : source) {
                long num = number.longValue();
                while (num != 0) {
                    bits.add(num & 1);
                    num >>= 1;
                }
            }
            return bits.toArray(new Long[0]);
        }
        String[] strings = new String[source.length];
        for (int i = 0; i < source.length; i++) {
            long num = source[i].longValue();
            strings[i] = Long.toBinaryString(num);
        }
        return strings;
    }

    @Override
    public Class<?> getReturnType() {
        if (isArray) return Long.class;
        else return String.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the bit " + (isArray ? "array" : "string") + " of " + getExpr().toString(e, debug);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setExpr((Expression<? extends Number>) exprs[0]);
        isArray = parseResult.hasTag("array");
        return true;
    }
}
