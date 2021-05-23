package commands;
import game.Game;
import org.bukkit.Bukkit;
import org.bukkit.command.*;

public class StartGameCommandExecutor implements CommandExecutor {
    Game game;

    public StartGameCommandExecutor(Game game){
        this.game = game;
    }

    private static final String[] default_settings = {"total_duration", "pact_duration", "initial_size", "final_size"};

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        ChangeGameSettings changeGameSettings = new ChangeGameSettings(game);
        if(args.length < 4) return false;
        for(int i = 0; i < default_settings.length; i++){
            if(changeGameSettings.parseArgument(args[i]) == null){
                args[i] = default_settings[i]+"="+args[i];
            }
        }
        changeGameSettings.applySettingsList(args);
        sender.sendMessage(ShowSettings.getConfigMessage(game.gameSettings));
        return true;
    }
}