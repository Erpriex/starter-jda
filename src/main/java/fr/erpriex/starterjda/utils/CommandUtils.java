package fr.erpriex.starterjda.utils;

public class CommandUtils {

    public String[] parseArgs(String command) {
        String[] commandSplit = command.split(" ");
        String[] args = new String[commandSplit.length-1];
        for(int i = 1; i < commandSplit.length; i++) args[i-1] = commandSplit[i];
        return args;
    }

}
