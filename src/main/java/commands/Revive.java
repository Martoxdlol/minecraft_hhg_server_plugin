package commands;

import game.Game;
import game.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Revive implements CommandExecutor {
    Game game;
    public Revive(Game game){
        this.game = game;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) return false;
        String playerName = args[0];
        GamePlayer player = game.getPlayer(playerName);

        if(player == null){
            Bukkit.broadcastMessage(ChatColor.RED+"No se encontró el jugador");
            return true;
        }else if(player.isAlive()){
            Bukkit.broadcastMessage(ChatColor.RED+"El jugador ya está vivo");
            return true;
        }

        boolean restoreInv = CommandsUtil.hasOption(args,"inventory") || CommandsUtil.hasOption(args,"restore_inventory");
        boolean keepEvent = CommandsUtil.hasOption(args,"keep-event") || CommandsUtil.hasOption(args,"keepevent");

        player.setLives(1);

        game.applyPlayerEffects(player);
        if(restoreInv && player.lastInventoryContent != null){
            player.player.getInventory().setContents(player.lastInventoryContent);
        }
        if(!keepEvent){
            game.status.resetLastDeathEvents(player);
        }
        player.player.teleport(player.lastDeathLocation);
        player.player.setExp(player.lastXp);

        return true;
    }
}
