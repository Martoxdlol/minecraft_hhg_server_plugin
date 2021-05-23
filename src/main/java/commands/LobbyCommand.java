package commands;

import game.GameWorld;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LobbyCommand implements CommandExecutor {
    GameWorld gameWorld;
    public LobbyCommand(GameWorld gameWorld){
        this.gameWorld = gameWorld;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean notBuild = false;
        boolean notTeleport = false;
        boolean notBorder = false;
        for(String arg : args){
            switch (arg) {
                case "no_teleport":
                    notTeleport = true;
                    break;
                case "no_build":
                    notBuild = true;
                    break;
                case "no_border":
                    notBorder = true;
                    break;
            }
        }
        if(!notBuild){
            gameWorld.setLobbyPlatform();
        }
        if(!notTeleport){
            gameWorld.teleportToLobby();
        }
        if(!notBorder){
            gameWorld.setLobbyBorder();
        }
        return true;
    }
}
