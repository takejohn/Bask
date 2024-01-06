package jp.takejohn.bask.processor.util;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TypeUse {

    private final @NotNull String className;

    private final @NotNull TypeUse @NotNull[] genericParameters;

    public TypeUse(@NotNull String className, @NotNull TypeUse @NotNull... genericParameters) {
        this.className = className;
        this.genericParameters = genericParameters;
    }

    @Override
    public String toString() {
        if (genericParameters.length == 0) {
            return className;
        } else {
            return Arrays.stream(genericParameters).map(TypeUse::toString)
                    .collect(Collectors.joining(",", className + "<", ">"));
        }
    }

}
