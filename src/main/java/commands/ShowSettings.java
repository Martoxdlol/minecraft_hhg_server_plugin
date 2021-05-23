package commands;

import game.GameSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ShowSettings implements CommandExecutor {
    GameSettings gameSettings;
    public ShowSettings(GameSettings gameSettings){
        this.gameSettings = gameSettings;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String message = getConfigMessage();
        sender.sendMessage(message);
        for(String arg : args){
            if(arg.equals("-broadcast") || arg.equals("--broadcast")){
                Bukkit.broadcastMessage(message);
            }
        }
        return true;
    }

    public static String getConfigMessage(GameSettings gameSettings){
        String message = "\n\n\n\n-------------------------------------------";
        message += "\n"+ChatColor.BOLD+""+ChatColor.DARK_AQUA+"CONFIGURACIÓN ACTUAL DEL JUEGO"+ChatColor.RESET;
        message += "\n-------------------------------------------\n";
        message += ChatColor.AQUA + "\ninitial_size: "+ ChatColor.GREEN + gameSettings.initialSize + ChatColor.GOLD +"\n(Tamaño del juego antes del deadmatch)\n";
        message += ChatColor.AQUA + "\nfinal_size: "+ ChatColor.GREEN + gameSettings.finalSize + ChatColor.GOLD +"\n(Tamaño del juego en el deadmatch)\n";
        message += ChatColor.AQUA + "\ntotal_duration: "+ ChatColor.GREEN + gameSettings.totalDuration + ChatColor.GOLD +"\n(Duración total de la partida)\n";
        message += ChatColor.AQUA + "\npact_duration: "+ ChatColor.GREEN + gameSettings.pactDuration + ChatColor.GOLD +"\n(Duración del tiempo de pacto.)\n";
        message += ChatColor.AQUA + "\npvp_on_pact: "+ ChatColor.GREEN + gameSettings.pvpOnPact + ChatColor.GOLD +"\n(Pvp activado en el pacto activado)\n";
        message += ChatColor.AQUA + "\ninitial_time: "+ ChatColor.GREEN + gameSettings.initialTime + ChatColor.GOLD +"\n(Tiempo del dia al arrancar la partida)\n";
        message += ChatColor.AQUA + "\ntime_on_pre_game: "+ ChatColor.GREEN + gameSettings.timeOnPreGame + ChatColor.GOLD +"\n(Tiempo del dia antes de arrancar el juego)\n";
        message += ChatColor.AQUA + "\ndo_day_cycle: "+ ChatColor.GREEN + gameSettings.doDayCycle + ChatColor.GOLD +"\n(Ciclo del día en juego activado)\n";
        message += ChatColor.AQUA + "\ndo_day_cycle_on_pre_game: "+ ChatColor.GREEN + gameSettings.doDayCycleOnPreGame + ChatColor.GOLD +"\n(Ciclo del día antes de arrancar activado)\n";
        message += ChatColor.AQUA + "\nshow_advancements: "+ ChatColor.GREEN + gameSettings.showAdvancements + ChatColor.GOLD +"\n(Mostrar logros)\n";
        return message;
    }

    public String getConfigMessage(){
        return ShowSettings.getConfigMessage(gameSettings);
    }
}
