package commands;

        import game.Game;
        import game.GamePlayer;
        import game.GameTeam;
        import game.GameWorld;
        import org.bukkit.Bukkit;
        import org.bukkit.ChatColor;
        import org.bukkit.command.Command;
        import org.bukkit.command.CommandExecutor;
        import org.bukkit.command.CommandSender;

        import java.util.List;

public class ShowPlayers implements CommandExecutor {
    Game game;
    public ShowPlayers(Game game){
        this.game = game;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        StringBuilder message = new StringBuilder();
        message.append("-------------------------------------------\n");
        message.append(ChatColor.AQUA).append("JUGADORES DEL JUEGO").append(ChatColor.RESET);
        message.append("\n-------------------------------------------\n\n");

        boolean broadcast = CommandsUtil.hasOption(args,"broadcast");
        boolean deathLocation = CommandsUtil.hasOption(args,"death_location") || CommandsUtil.hasOption(args,"death_locations");

        game.status.setPlayersDisplayNames();

        for(GamePlayer player : game.getPlayers()){
            message.append(player.player.getDisplayName()).append(ChatColor.RESET).append("\n");
            message.append(" - vidas: ").append(ChatColor.GREEN).append(player.getLives()).append(ChatColor.RESET).append("\n");
            message.append(" - equipo: ").append(ChatColor.GREEN).append(player.team.getDisplayName()).append(ChatColor.RESET).append("\n");
           if(deathLocation && player.lastDeathLocation != null ){
               message.append(" - pos muerte: ").append(ChatColor.BLUE).append(GameWorld.locationToText(player.lastDeathLocation)).append(ChatColor.RESET).append("\n");
           }
        }

        message.append(ChatColor.RESET);
        message.append("-------------------------------------------");

        String finalMessage = message.toString();

        if(broadcast){
            Bukkit.broadcastMessage(finalMessage);
        }else {
            sender.sendMessage(finalMessage);
        }

        return true;
    }
}
