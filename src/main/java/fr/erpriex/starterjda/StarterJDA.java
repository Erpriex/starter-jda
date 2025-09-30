package fr.erpriex.starterjda;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import fr.erpriex.starterjda.commands.CommandStop;
import fr.erpriex.starterjda.commands.CommandTest;
import fr.erpriex.starterjda.commands.construct.CommandMap;
import fr.erpriex.starterjda.listeners.CommandListener;
import fr.erpriex.starterjda.listeners.ReadyListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.InputStreamReader;
import java.util.Scanner;

public class StarterJDA implements Runnable {

    private JDA jda;
    private CommandMap commandMap;

    private final Scanner scanner = new Scanner(System.in);
    private boolean running;

    public StarterJDA() throws InterruptedException {
        Gson gson = new GsonBuilder().create();
        JsonObject jsonObject = gson.fromJson(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("config.json")), JsonObject.class);
        String token = jsonObject.get("token").getAsString();
        String commandPrefix = jsonObject.get("commandPrefix").getAsString();

        commandMap = new CommandMap(this, commandPrefix);

        commandMap.registerCommand(new CommandStop(this));

        jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new CommandListener(this))
                .addEventListeners(new ReadyListener())
                .build();

        jda.awaitReady();

        commandMap.registerSlashCommands();
    }

    public static void main(String[] args) {
        try {
            StarterJDA starterJDA = new StarterJDA();
            new Thread(starterJDA, "bot").start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        running = true;

        while(running){
            if(scanner.hasNextLine()){
                commandMap.commandConsole(scanner.nextLine());
            }
        }

        scanner.close();
        System.out.println("Arret du bot !");
        jda.shutdown();
        System.exit(0);
    }

    public void stopBot(){
        running = false;
    }

    public JDA getJDA(){
        return jda;
    }

    public CommandMap getCommandMap(){
        return commandMap;
    }

}
