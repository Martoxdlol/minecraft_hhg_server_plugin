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
        if(args.length == 0) return false;
        if(args[0].equals("*")){
            sender.sendMessage(ChatColor.GREEN+"Se agregaron "+ChatColor.AQUA+""+addAll(game)+""+ChatColor.GREEN+" jugadores al juego");
            return true;
        }
        Player player = Bukkit.getServer().getPlayer(args[0]);
        GamePlayer gamePlayer = game.addPlayer(player);
        if(gamePlayer == null){
            if(game.getPlayer(player) != null){
                sender.sendMessage(ChatColor.RED+"No se pudo agregar el jugador. Ya está en el juego)");
                return true;
            }else if(player == null){
                sender.sendMessage(ChatColor.RED+"No se encontró el jugador");
                return true;
            }
            return false;
        }
        sender.sendMessage(ChatColor.GREEN+"Se agregó "+ChatColor.AQUA+gamePlayer.player.getName()+ChatColor.GREEN+" al juego");
        return true;
    }

    public static int addAll(Game game){
        int count = 0;
        for(Player player : Bukkit.getServer().getOnlinePlayers()){
            GamePlayer gamePlayer = game.addPlayer(player);
            if(gamePlayer != null) {
                count++;
                if(game.inGame()){
                    game.applyPlayerEffects(gamePlayer);
                }
            }
        }
        return count;
    }
}
