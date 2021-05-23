package commands;

import game.Game;
import game.GameEvent;
import game.GameStatus;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ShowGameStatus implements CommandExecutor {
    Game game;

    public ShowGameStatus(Game game){
        this.game = game;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        GameStatus status = game.status;
        boolean broadcast = false;
        String message = "";

        for(GameEvent event : game.status.getEvents()){
            message += "\n"+event.getMessage();
        }

        for(String arg : args){
            switch (arg) {
                case "-broadcast":
                    broadcast = true;
                    break;
            }
        }
        if(broadcast){
            Bukkit.broadcastMessage(message);
        }else{
            sender.sendMessage(message);
        }
        return true;
    }
}
