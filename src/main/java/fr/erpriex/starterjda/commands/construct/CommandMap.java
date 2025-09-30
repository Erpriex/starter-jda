package fr.erpriex.starterjda.commands.construct;

import fr.erpriex.starterjda.StarterJDA;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class CommandMap {

    private StarterJDA main;

    private final Map<String, SimpleCommand> commands = new HashMap<>();
    private final String tag;

    public CommandMap(StarterJDA main, String commandPrefix) {
        this.main = main;
        this.tag = commandPrefix;
    }

    public String getTag() {
        return tag;
    }

    public Collection<SimpleCommand> getCommands(){
        return commands.values();
    }

    public void registerCommands(Object...objects){
        for(Object object : objects) registerCommand(object);
    }

    public void registerCommand(Object object){
        for(Method method : object.getClass().getDeclaredMethods()){
            if(method.isAnnotationPresent(Command.class)){
                Command command = method.getAnnotation(Command.class);
                method.setAccessible(true);
                SimpleCommand simpleCommand = new SimpleCommand(
                    command.name(), 
                    command.description(), 
                    command.type(), 
                    object, 
                    method,
                    command.isSlashCommand(),
                    command.options()
                );
                commands.put(command.name(), simpleCommand);
                if (command.alias().length > 0) {
                    for (String alias : command.alias()) {
                        commands.put(alias, simpleCommand);
                    }
                }
            }
        }
    }

    public void registerSlashCommands() {
        List<CommandData> slashCommands = commands.values().stream()
            .filter(SimpleCommand::isSlashCommand)
            .filter(cmd -> cmd.getExecutorType() != Command.ExecutorType.CONSOLE)
            .map(SimpleCommand::toCommandData)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        if (!slashCommands.isEmpty()) {
            main.getJDA().updateCommands().addCommands(slashCommands).queue();
            System.out.println("Commandes slash enregistrées: " + slashCommands.size());
        }
    }

    public void handleSlashCommand(SlashCommandInteractionEvent event) {
        SimpleCommand simpleCommand = commands.get(event.getName());
        if (simpleCommand == null || simpleCommand.getExecutorType() == Command.ExecutorType.CONSOLE) {
            event.reply("Commande non trouvée.").setEphemeral(true).queue();
            return;
        }

        try {
            executeSlashCommand(simpleCommand, event);
        } catch (Exception exception) {
            System.out.println("Une erreur est survenue dans la commande slash " + simpleCommand.getMethod().getName());
            exception.printStackTrace();
            event.reply("Une erreur est survenue lors de l'exécution de la commande.").setEphemeral(true).queue();
        }
    }

    public void commandConsole(String command) {
        Object[] object = getCommand(command);
        if (object[0] == null || ((SimpleCommand) object[0]).getExecutorType() == Command.ExecutorType.USER) {
            System.out.println("Commande inconnue.");
            return;
        }
        try {
            execute(((SimpleCommand) object[0]), command, (String[]) object[1], null);
        } catch (Exception exception) {
            System.out.println("Une erreur est survenue dans la commande " + ((SimpleCommand) object[0]).getMethod().getName());
        }
    }

    public boolean commandUser(User user, String command, Message message) {
        Object[] object = getCommand(command);
        if (object[0] == null || ((SimpleCommand) object[0]).getExecutorType() == Command.ExecutorType.CONSOLE)
            return false;
        try {
            execute(((SimpleCommand) object[0]), command, (String[]) object[1], message);
        } catch (Exception exception) {
            System.out.println("Une erreur est survenue dans la commande " + ((SimpleCommand) object[0]).getMethod().getName());
        }
        return true;
    }

    private Object[] getCommand(String command) {
        String[] commandSplit = command.split(" ");
        String[] args = new String[commandSplit.length - 1];
        for (int i = 1; i < commandSplit.length; i++)
            args[i - 1] = commandSplit[i];
        SimpleCommand simpleCommand = commands.get(commandSplit[0]);
        return new Object[] { simpleCommand, args };
    }

    private void execute(SimpleCommand simpleCommand, String command, String[] args, Message message)
            throws Exception {
        Parameter[] parameters = simpleCommand.getMethod().getParameters();
        Object[] objects = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getType() == String[].class)
                objects[i] = args;
            else if (parameters[i].getType() == User.class)
                objects[i] = message == null ? null : message.getAuthor();
            else if (parameters[i].getType() == TextChannel.class)
                objects[i] = message == null ? null : message.getChannel().asTextChannel();
            else if (parameters[i].getType() == PrivateChannel.class)
                objects[i] = message == null ? null : message.getChannel().asPrivateChannel();
            else if (parameters[i].getType() == Guild.class)
                objects[i] = message == null ? null : message.getGuild();
            else if (parameters[i].getType() == String.class)
                objects[i] = command;
            else if (parameters[i].getType() == Message.class)
                objects[i] = message;
            else if (parameters[i].getType() == JDA.class)
                objects[i] = main.getJDA();
            else if (parameters[i].getType() == MessageChannelUnion.class)
                objects[i] = message.getChannel();
        }
        simpleCommand.getMethod().invoke(simpleCommand.getObject(), objects);
    }

    private void executeSlashCommand(SimpleCommand simpleCommand, SlashCommandInteractionEvent event) throws Exception {
        Parameter[] parameters = simpleCommand.getMethod().getParameters();
        Object[] objects = new Object[parameters.length];
        
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getType() == SlashCommandInteractionEvent.class) {
                objects[i] = event;
            } else if (parameters[i].getType() == User.class) {
                objects[i] = event.getUser();
            } else if (parameters[i].getType() == TextChannel.class) {
                objects[i] = event.getChannel() != null ? event.getChannel().asTextChannel() : null;
            } else if (parameters[i].getType() == Guild.class) {
                objects[i] = event.getGuild();
            } else if (parameters[i].getType() == JDA.class) {
                objects[i] = main.getJDA();
            } else if (parameters[i].getType() == String.class) {
                String paramName = parameters[i].getName();
                OptionMapping option = event.getOption(paramName);
                objects[i] = option != null ? option.getAsString() : null;
            } else if (parameters[i].getType() == Integer.class || parameters[i].getType() == int.class) {
                String paramName = parameters[i].getName();
                OptionMapping option = event.getOption(paramName);
                objects[i] = option != null ? option.getAsInt() : 0;
            } else if (parameters[i].getType() == Long.class || parameters[i].getType() == long.class) {
                String paramName = parameters[i].getName();
                OptionMapping option = event.getOption(paramName);
                objects[i] = option != null ? option.getAsLong() : 0L;
            } else if (parameters[i].getType() == Boolean.class || parameters[i].getType() == boolean.class) {
                String paramName = parameters[i].getName();
                OptionMapping option = event.getOption(paramName);
                objects[i] = option != null ? option.getAsBoolean() : false;
            }
        }
        
        simpleCommand.getMethod().invoke(simpleCommand.getObject(), objects);
    }
}
