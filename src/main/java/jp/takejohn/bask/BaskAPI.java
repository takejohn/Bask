package jp.takejohn.bask;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import jp.takejohn.bask.annotations.*;
import jp.takejohn.bask.classes.ContextParser;
import jp.takejohn.bask.util.Primitives;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.arithmetic.Arithmetics;
import org.skriptlang.skript.lang.arithmetic.Operation;
import org.skriptlang.skript.lang.arithmetic.Operator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class BaskAPI {

    private abstract static class ToStringParser<T> extends Parser<T> {

        @Override
        public @NotNull String toString(@NotNull T o, int flags) {
            return toVariableNameString(o);
        }

        @Override
        public @NotNull String toVariableNameString(@NotNull T o) {
            return o.toString();
        }

    }

    private abstract static class OperationMethod<L, R, T> implements Operation<L, R, T> {

        public final Class<L> leftType;

        public final Class<R> rightType;

        public final Class<T> returnType;

        private OperationMethod(Class<L> leftType, Class<R> rightType, Class<T> returnType) {
            this.leftType = leftType;
            this.rightType = rightType;
            this.returnType = returnType;
        }

        public abstract T invoke(L left, R right) throws IllegalAccessException, InvocationTargetException;

        @Override
        public T calculate(@NotNull L left, @NotNull R right) {
            try {
                return invoke(left, right);
            } catch (InvocationTargetException e) {
                Skript.error("Operation failed: " + e.getTargetException());
                return null;
            } catch (IllegalAccessException e) {
                throw new BaskAPIException(e);
            }
        }

    }

    private BaskAPI() {
        throw new AssertionError();
    }

    /**
     * Register a class as a type in Skript if it has a {@link SkriptType} annotation.
     * The class may have a {@link SkriptDoc} annotation for Skript's documentation.
     * @param clazz a class to register
     * @param parser a parser for the type
     */
    public static <T> void register(@NotNull Class<T> clazz, @NotNull Parser<T> parser) {
        Objects.requireNonNull(clazz, "clazz cannot be null");
        Objects.requireNonNull(parser, "parser cannot be null");

        final @Nullable SkriptType skriptType = clazz.getAnnotation(SkriptType.class);
        if (skriptType != null) {
            final @NotNull ClassInfo<T> classInfo = new ClassInfo<>(clazz, skriptType.value());
            classInfo.user(skriptType.user());
            setDoc(classInfo, clazz.getAnnotation(SkriptDoc.class));
            setDoc(classInfo, clazz.getAnnotation(SkriptTypeUsage.class));
            classInfo.parser(parser);
            Classes.registerClass(classInfo);
        }

        registerArithmetics(clazz);
    }

    /**
     * Register a class as a type in Skript if it has a {@link SkriptType} annotation.
     * The class may have a {@link SkriptDoc} annotation for Skript's documentation.
     * Some static methods of the class can be marked with {@link SkriptTypeParse} annotation
     * to parse a string into the type.
     * @param clazz a class to register
     */
    public static <T> void register(@NotNull Class<T> clazz) {
        register(clazz, getContextParser(clazz));
    }

    /**
     * Register a class as a type in Skript if it has a {@link SkriptType} annotation.
     * The class may have a {@link SkriptDoc} annotation for Skript's documentation.
     * @param clazz a class to register
     * @param contextParser a {@link ContextParser} to parse a string into the type
     */
    public static <T> void register(@NotNull Class<T> clazz, @NotNull ContextParser<T> contextParser) {
        register(clazz, new ToStringParser<>() {

            @Override
            public @Nullable T parse(@NotNull String s, @NotNull ParseContext context) {
                return contextParser.parse(s, context);
            }

            @Override
            public boolean canParse(@NotNull ParseContext context) {
                return contextParser.canParse(context);
            }

        });
    }

    private static void setDoc(@NotNull ClassInfo<?> classInfo, @Nullable SkriptDoc skriptDoc) {
        if (skriptDoc == null) {
            return;
        }
        if (!skriptDoc.name().isEmpty()) {
            classInfo.name(skriptDoc.name());
        }
        if (skriptDoc.description().length != 0) {
            classInfo.description(skriptDoc.description());
        }
        if (skriptDoc.examples().length != 0) {
            classInfo.examples(skriptDoc.examples());
        }
        if (!skriptDoc.since().isEmpty()) {
            classInfo.since(skriptDoc.since());
        }
        if (skriptDoc.requiredPlugins().length != 0) {
            classInfo.requiredPlugins(skriptDoc.requiredPlugins());
        }
        if (!skriptDoc.documentationId().isEmpty()) {
            classInfo.documentationId(skriptDoc.documentationId());
        }
    }

    private static void setDoc(@NotNull ClassInfo<?> classInfo, @Nullable SkriptTypeUsage skriptTypeUsage) {
        if (skriptTypeUsage != null) {
            classInfo.usage(skriptTypeUsage.value());
        }
    }

    private static <T> @NotNull ContextParser<T> getContextParser(
            @NotNull Class<T> clazz) {
        final EnumMap<ParseContext, @NotNull Method> methodMap = new EnumMap<>(ParseContext.class);
        for (Method method : clazz.getDeclaredMethods()) {
            final @Nullable SkriptTypeParse skriptTypeParse = method.getAnnotation(SkriptTypeParse.class);
            if (skriptTypeParse == null) {
                continue;
            }
            if (!Modifier.isStatic(method.getModifiers())) {
                throw new BaskAPIException(MessageFormat.format(
                        "Class {0} has a non-static method {1} annotated with @{2}",
                        clazz.getName(), method, SkriptTypeParse.class.getName()));
            }
            for (SkriptParseContext context : skriptTypeParse.value()) {
                final @NotNull ParseContext parseContext = context.asParseContext();
                if (methodMap.containsKey(parseContext)) {
                    throw new BaskAPIException(MessageFormat.format(
                            "Class {0} has more than one method to parse in {1} context",
                            clazz.getName(), context
                    ));
                }
                methodMap.put(parseContext, method);
            }
        }
        return asContextParser(methodMap, clazz);
    }

    @SuppressWarnings("unchecked")
    private static <T> @NotNull ContextParser<T> asContextParser(
            @NotNull Map<ParseContext, @NotNull Method> methodMap, @NotNull Class<T> returnType) {
        for (@NotNull Method method : methodMap.values()) {
            requireParseMethodType(method, returnType);
        }
        return new ContextParser<>() {

            @Override
            public boolean canParse(@NotNull ParseContext context) {
                return methodMap.containsKey(context);
            }

            @Override
            public T parse(@NotNull String s, @NotNull ParseContext context) {
                try {
                    return (T) methodMap.get(context).invoke(null, s);
                } catch (InvocationTargetException e) {
                    final @NotNull Throwable targetException = e.getTargetException();
                    Skript.error(MessageFormat.format(
                            "\"{0}\" cannot be parsed as a valid {1}: {2}",
                            s, Objects.requireNonNull(Classes.getExactClassInfo(returnType)).getCodeName(),
                            targetException.toString()));
                    return null;
                } catch (IllegalAccessException e) {
                    throw new BaskAPIException(e);
                }
            }

        };
    }

    private static <T> void requireParseMethodType(@NotNull Method method, @NotNull Class<T> returnType) {
        final @NotNull Class<?> @NotNull[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 1 || !parameterTypes[0].isAssignableFrom(String.class)) {
            throw new BaskAPIException(MessageFormat.format(
                    "Method {0} annotated with @{1} in class {2} must have one parameter accepting a string",
                    method.getName(), SkriptTypeParse.class.getName(), method.getDeclaringClass().getName()));
        }
        if (!returnType.isAssignableFrom(method.getReturnType())) {
            throw new BaskAPIException(MessageFormat.format(
                    "Method {0} annotated with @{1} in class {2} must return a(n) {3}",
                    method.getName(), SkriptTypeParse.class.getName(), method.getDeclaringClass().getName(),
                    returnType.getName()));
        }
    }

    public static <T> void registerArithmetics(@NotNull Class<T> clazz) {
        for (@NotNull Method method : clazz.getDeclaredMethods()) {
            final @Nullable SkriptOperation skriptOperation = method.getAnnotation(SkriptOperation.class);
            if (skriptOperation == null) {
                continue;
            }
            registerOperation(skriptOperation.value().asOperator(), asOperationMethod(method), skriptOperation.commutative());
        }
    }

    @SuppressWarnings("unchecked")
    private static <L, R, T> @NotNull OperationMethod<L, R, T> asOperationMethod(@NotNull Method method) {
        final @NotNull Class<T> returnType = (Class<T>) Primitives.wrap(method.getReturnType());
        if (Modifier.isStatic(method.getModifiers())) {
            final @NotNull Class<?> @NotNull[] parameterTypes = requireMethodParameterCount(method, 2);
            final @NotNull Class<L> leftType = (Class<L>) Primitives.wrap(parameterTypes[0]);
            final @NotNull Class<R> rightType = (Class<R>) Primitives.wrap(parameterTypes[1]);
            return new OperationMethod<>(leftType, rightType, returnType) {

                @Override
                public Object invoke(Object left, Object right) throws IllegalAccessException, InvocationTargetException {
                    return method.invoke(null, left, right);
                }

            };
        } else {
            final @NotNull Class<R> rightType = (Class<R>) Primitives.wrap(requireMethodParameterCount(method, 1)[0]);
            return new OperationMethod<>((Class<L>) method.getDeclaringClass(), rightType, returnType) {

                @Override
                public Object invoke(Object left, Object right) throws IllegalAccessException, InvocationTargetException {
                    return method.invoke(left, right);
                }

            };
        }
    }

    private static @NotNull Class<?> @NotNull[] requireMethodParameterCount(@NotNull Method method, int count) {
        final @NotNull Class<?> @NotNull[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != count) {
            throw new BaskAPIException(MessageFormat.format(
                    "Method {0} annotated with @{1} in class {2} must have {3} parameter(s)",
                    method.getName(), SkriptOperation.class.getName(), method.getDeclaringClass().getName(), count));
        }
        return parameterTypes;
    }

    private static <L, R, T> void registerOperation(Operator operator, @NotNull OperationMethod<L, R, T> operationMethod,
                                                    boolean commutative) {
        Arithmetics.registerOperation(
                operator, operationMethod.leftType, operationMethod.rightType, operationMethod.returnType, operationMethod);
        if (commutative && !operationMethod.leftType.equals(operationMethod.rightType)) {
            Arithmetics.registerOperation(
                    operator, operationMethod.rightType, operationMethod.leftType, operationMethod.returnType,
                    ((left, right) -> operationMethod.calculate(right, left)));
        }
    }

}
