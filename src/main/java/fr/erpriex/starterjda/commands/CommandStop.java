package fr.erpriex.starterjda.commands;

import fr.erpriex.starterjda.StarterJDA;
import fr.erpriex.starterjda.commands.construct.Command;

public class CommandStop {

    private StarterJDA main;

    public CommandStop(StarterJDA main){
        this.main = main;
    }

    @Command( name = "stop", description = "Arrêter le bot", type = Command.ExecutorType.CONSOLE, isSlashCommand = true)
    public void command(){
        main.stopBot();
    }

}
