package fr.erpriex.starterjda.commands.construct;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value= ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    public String name();
    public String[] alias() default {};
    public String description() default "Aucune description !";
    public ExecutorType type() default ExecutorType.USER;

    public enum ExecutorType{
        ALL, USER, CONSOLE;
    }

}
