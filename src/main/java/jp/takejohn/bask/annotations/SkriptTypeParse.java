package jp.takejohn.bask.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a static method should be used as a parser of a type in Skript.
 * This annotation must be used to mark at most one method in a single class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SkriptTypeParse {
    /* marker annotation */
}
