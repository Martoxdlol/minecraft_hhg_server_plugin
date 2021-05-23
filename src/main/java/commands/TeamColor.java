package commands;

import game.Game;
import game.GameTeam;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TeamColor implements CommandExecutor {
    Game game;
    public TeamColor(Game game){
        this.game = game;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        StringBuilder name = new StringBuilder();
        String colorName = "";
        boolean first = true;
        for(String arg : args){
            if(first){
                colorName = arg;
                first = false;
            }else{
                name.append(" ").append(arg);
            }
        }
        GameTeam team = game.getTeam(name.toString());
        if(team != null){
            ChatColor newColor = ChatColor.getByChar(colorName);
            if(newColor == null){
                sender.sendMessage(ChatColor.RED+"Color "+colorName+" inválido");
                return false;
            }
            team.color = newColor;
            sender.sendMessage("Color de "+ team.color+team.name+ChatColor.RESET+" cambiado");
            return true;
        }
        return false;
    }
}