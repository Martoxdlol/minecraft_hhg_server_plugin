package commands;

import game.Game;
import game.GameSettings;
import game.GameTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AddTeam implements CommandExecutor {
    Game game;
    public AddTeam(Game game){
        this.game = game;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        StringBuilder name = new StringBuilder();
        for(String arg : args){
            name.append(" ").append(arg);
        }
        String teamName = name.toString().trim();
        GameTeam team = game.addTeam(teamName);
        if(team != null){
            sender.sendMessage("Team "+ChatColor.GOLD+team.name+ChatColor.RESET+" agregado");
            return true;
        }
        return false;
    }
}
