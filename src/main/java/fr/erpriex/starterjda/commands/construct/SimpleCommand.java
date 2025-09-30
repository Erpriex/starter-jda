package fr.erpriex.starterjda.commands.construct;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.lang.reflect.Method;

public class SimpleCommand {

    private final String name, description;
    private Command.ExecutorType executorType;
    private final Object object;
    private final Method method;
    private final boolean isSlashCommand;
    private final Command.SlashOption[] slashOptions;

    public SimpleCommand(String name, String description, Command.ExecutorType executorType, Object object, Method method, boolean isSlashCommand, Command.SlashOption[] slashOptions) {
        this.name = name;
        this.description = description;
        this.executorType = executorType;
        this.object = object;
        this.method = method;
        this.isSlashCommand = isSlashCommand;
        this.slashOptions = slashOptions;
    }

    public Command.ExecutorType getExecutorType() {
        return executorType;
    }

    public void setExecutorType(Command.ExecutorType executorType) {
        this.executorType = executorType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Object getObject() {
        return object;
    }

    public Method getMethod() {
        return method;
    }

    public boolean isSlashCommand() {
        return isSlashCommand;
    }

    public Command.SlashOption[] getSlashOptions() {
        return slashOptions;
    }

    public CommandData toCommandData() {
        if (!isSlashCommand) return null;

        SlashCommandData data = Commands.slash(name, description);

        if (slashOptions != null) {
            for (Command.SlashOption opt : slashOptions) {
                data.addOptions(new OptionData(opt.type(), opt.name(), opt.description(), opt.required()));
            }
        }
        return data;
    }
}
