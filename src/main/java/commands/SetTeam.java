package commands;

import game.Game;
import game.GamePlayer;
import game.GameSettings;
import game.GameTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetTeam implements CommandExecutor {
    Game game;
    public SetTeam(Game game){
        this.game = game;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        StringBuilder name = new StringBuilder();
        String playerName = "";
        boolean first = true;
        for(String arg : args){
            if(first){
                playerName = arg;
                first = false;
            }else{
                name.append(" ").append(arg);
            }
        }
        if(playerName.equals("")){
            sender.sendMessage(ChatColor.RED+"Nombre del jugador inválido");
            return false;
        }
        String teamName = name.toString().trim();
        GameTeam team = game.getTeam(teamName);
        GamePlayer player = game.getPlayer(playerName);
        if(team == null) {
            sender.sendMessage(ChatColor.RED + "Nombre del equipo inválido");
            return false;
        }
        if(player == null) {
            Player bukkitPlayer = Bukkit.getServer().getPlayer(playerName);
            player = game.addPlayer(bukkitPlayer);
            if(bukkitPlayer == null){
                sender.sendMessage(ChatColor.RED + "Nombre del jugador inválido");
                return false;
            }else{
                sender.sendMessage(ChatColor.GOLD + player.player.getName() + ChatColor.RESET + " agregado al juego");
            }
        }
        player.team = team;
        sender.sendMessage(ChatColor.GREEN+player.player.getName()+" agregado a "+team.color+team.name);
        return true;
    }
}
