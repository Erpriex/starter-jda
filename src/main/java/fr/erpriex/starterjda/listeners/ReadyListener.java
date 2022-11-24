package fr.erpriex.starterjda.listeners;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class ReadyListener implements EventListener {


    @Override
    public void onEvent(GenericEvent event) {
        if(event instanceof ReadyEvent){
            onReady((ReadyEvent) event);
        }
    }

    public void onReady(ReadyEvent event){
        System.out.println("Bot ready !");
    }
}
