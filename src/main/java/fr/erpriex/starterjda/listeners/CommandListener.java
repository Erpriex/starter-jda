package fr.erpriex.starterjda.listeners;

import fr.erpriex.starterjda.StarterJDA;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class CommandListener implements EventListener {

    private StarterJDA main;

    public CommandListener(StarterJDA main) {
        this.main = main;
    }


    @Override
    public void onEvent(GenericEvent event) {
        if(event instanceof MessageReceivedEvent){
            onCommand((MessageReceivedEvent) event);
        }
    }

    public void onCommand(MessageReceivedEvent event){
        if(event.getAuthor().equals(event.getJDA().getSelfUser())) {
            return;
        }

        String message = event.getMessage().getContentDisplay();
        if(message.startsWith(main.getCommandMap().getTag())) {
            message = message.substring(main.getCommandMap().getTag().length());
            if(main.getCommandMap().commandUser(event.getAuthor(), message, event.getMessage())) {

            }
        }
    }
}
