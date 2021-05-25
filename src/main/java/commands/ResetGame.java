package commands;

import game.Game;
import net.tomascichero.hhg_games.Hhg_games;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;



public class ResetGame implements CommandExecutor {
    Game game;
    public ResetGame(Game game){
        this.game = game;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage("El juego ha sido reseteado");
        game.reset();
        return true;
    }
}
