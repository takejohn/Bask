package jp.takejohn.bask.annotations;

import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.lang.arithmetic.Operator;

/**
 * Declares a
 */
public enum SkriptOperator {

    ADDITION(Operator.ADDITION),
    SUBTRACTION(Operator.SUBTRACTION),
    MULTIPLICATION(Operator.MULTIPLICATION),
    DIVISION(Operator.DIVISION),
    EXPONENTIATION(Operator.EXPONENTIATION);

    final @NotNull Operator operator;

    SkriptOperator(@NotNull Operator operator) {
        this.operator = operator;
    }

    public @NotNull Operator asOperator() {
        return operator;
    }

}
