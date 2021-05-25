package commands;

import game.Game;
import game.GamePlayer;
import game.GameTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ShowTeams implements CommandExecutor {
    Game game;
    public ShowTeams(Game game){
        this.game = game;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        StringBuilder message = new StringBuilder();
        message.append("-------------------------------------------\n");
        message.append(ChatColor.AQUA).append("EQUIPOS DEL JUEGO").append(ChatColor.RESET);
        message.append("\n-------------------------------------------\n\n");
        for(GameTeam team : game.getTeams()){
            message.append(ChatColor.RESET).append("EQUIPO: ").append(team.color).append(team.name).append("\n");
            List<GamePlayer> players = team.getMembers();
            for(GamePlayer player : players){
                message.append(" - ").append(team.color).append(player.player.getName()).append(ChatColor.RESET).append("\n");
            }
            message.append("\n");
            message.append("\n");
        }
        message.append(ChatColor.RESET);
        message.append("-------------------------------------------");
        message.append("\n");
        message.append("\n");

        String finalMessage = message.toString();

        boolean broadcast = CommandsUtil.hasOption(args,"broadcast");

        if(broadcast){
            Bukkit.broadcastMessage(finalMessage);
        }else {
            sender.sendMessage(finalMessage);
        }

        game.status.setPlayersDisplayNames();

        return true;
    }
}
