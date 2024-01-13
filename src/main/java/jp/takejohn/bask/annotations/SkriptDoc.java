package jp.takejohn.bask.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SkriptDoc {

    String name() default "";

    String[] description() default {};

    String[] examples() default {};

    String since() default "";

    String[] requiredPlugins() default {};

    String documentationId() default "";

}
