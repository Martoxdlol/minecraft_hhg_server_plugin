package commands;

import game.Game;
import game.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddPlayer implements CommandExecutor {
    Game game;
    public AddPlayer(Game game){
        this.game = game;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = Bukkit.getServer().getPlayer(args[0]);
        GamePlayer gamePlayer = game.addPlayer(player);
        if(gamePlayer == null){
            sender.sendMessage(ChatColor.RED+"No se pudo agregar el jugador. (puede que ya esté en el juego)");
            return false;
        }
        sender.sendMessage(ChatColor.GREEN+"Se agregó "+ChatColor.AQUA+gamePlayer.player.getName()+ChatColor.GREEN+" al juego");
        return true;
    }
}
