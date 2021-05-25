package commands;

import game.Game;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnFinish implements CommandExecutor {
    Game game;
    public UnFinish(Game game){
        this.game = game;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(game.unFinish()){
            sender.sendMessage(ChatColor.GREEN+"Continuando el juego");
            return true;
        }
        sender.sendMessage(ChatColor.RED+"No se pudo des-finalizar el juego. Probá reviviendo a los muertos");
        return false;
    }
}