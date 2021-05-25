package commands;

import game.Game;
import game.GameEvent;
import game.GameStatus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ShowGameStatus implements CommandExecutor {
    Game game;

    public ShowGameStatus(Game game){
        this.game = game;
    }

    static String[] gameStateCodes = {"Listo para iniciar","En pacto","En juego","En deathmatch","Finalizado"};

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        GameStatus status = game.status;

        String message = "";
        message += "-------------------------------------------\n";
        message += (ChatColor.AQUA)+("ESTADO DEL JUEGO") + (ChatColor.RESET);
        message += "\n-------------------------------------------\n";
        message += "Equipos vivos "+ ChatColor.GREEN+status.teamsAlive().size()+ChatColor.RESET+"\n";
        message += "Jugadores vivos "+ ChatColor.GREEN+status.alivePlayers().size()+ChatColor.RESET+"\n";
        message += "Jugadores totales "+ ChatColor.GREEN+game.getPlayers().size()+ChatColor.RESET+"\n";
        message += "Estado: "+ChatColor.GREEN+gameStateCodes[game.getGameState()]+ChatColor.RESET+"\n";
        message += "Tiempo: "+ChatColor.GREEN+GameEvent.getTime(game.getTickNum())+ChatColor.RESET+"\n";



        boolean hideEvents = CommandsUtil.hasOption(args, "hide_events");

        if(!hideEvents){
            message += "\n-------------------------------------------\n";
            message += (ChatColor.AQUA)+("EVENTOS DEL JUEGO") + (ChatColor.RESET);
            message += "\n-------------------------------------------\n";
            for(GameEvent event : game.status.getEvents()){
                message += "\n"+event.getMessage();
            }
        }


        boolean broadcast = CommandsUtil.hasOption(args,"broadcast");
        if(broadcast){
            Bukkit.broadcastMessage(message);
        }else{
            sender.sendMessage(message);
        }
        return true;
    }
}
