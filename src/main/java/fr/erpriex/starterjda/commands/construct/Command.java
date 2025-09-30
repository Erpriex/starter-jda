package fr.erpriex.starterjda.commands.construct;

import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    public String name();
    public String[] alias() default {};
    public String description() default "Aucune description !";
    public ExecutorType type() default ExecutorType.USER;

    public boolean isSlashCommand() default true;
    public SlashOption[] options() default {};

    public enum ExecutorType {
        ALL, USER, CONSOLE;
    }

    @Target(value = ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SlashOption {
        String name();
        String description();
        OptionType type();
        boolean required() default false;
    }
}
