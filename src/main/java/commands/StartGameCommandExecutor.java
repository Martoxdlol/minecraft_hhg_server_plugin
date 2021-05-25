package commands;
import game.Game;
import org.bukkit.ChatColor;
import org.bukkit.command.*;

public class StartGameCommandExecutor implements CommandExecutor {
    Game game;

    public StartGameCommandExecutor(Game game){
        this.game = game;
    }

    private static final String[] default_settings = {"total_duration", "pact_duration", "initial_size", "final_size"};

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(game.inGame()){
            sender.sendMessage(ChatColor.RED+"El juego ya esta iniciado");
            sender.sendMessage(ChatColor.RESET+"Usá /reset_game y luego inicie un nuevo juego");
            return true;
        }
        boolean addAll = !CommandsUtil.hasOption(args,"no_add_playersta");
        if(game.getPlayers().isEmpty()){
            if(addAll) AddPlayer.addAll(game);
            //sender.sendMessage(ChatColor.RED+"Agrega jugadores primero: "+ChatColor.GOLD+"/add_player nombre"+ChatColor.RED+" o "+ChatColor.GOLD+"/add_player *");
        }
        ChangeGameSettings changeGameSettings = new ChangeGameSettings(game);
        if(args.length < 4) return false;
        for(int i = 0; i < default_settings.length; i++){
            if(changeGameSettings.parseArgument(args[i]) == null){
                args[i] = default_settings[i]+"="+args[i];
            }
        }
        changeGameSettings.applySettingsList(args);

        game.start();
        return true;
    }
}