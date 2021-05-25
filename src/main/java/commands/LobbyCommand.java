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
        boolean notBuild = CommandsUtil.hasOption(args,"no_build");
        boolean notTeleport = CommandsUtil.hasOption(args,"no_teleport");
        boolean notBorder = CommandsUtil.hasOption(args,"no_border");
        boolean notSetGameModes = CommandsUtil.hasOption(args,"no_gamemodes");
        if(!notBuild){
            gameWorld.setLobbyPlatform();
        }
        if(!notTeleport){
            gameWorld.teleportToLobby();
        }
        if(!notBorder){
            gameWorld.setLobbyBorder();
        }
        if(!notSetGameModes){
            gameWorld.setLobbyGameModes();
        }

        return true;
    }
}
