package commands;

import game.Game;
import game.GameSettings;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.*;

import java.util.Locale;

public class ChangeGameSettings implements CommandExecutor {
    public GameSettings gameSettings;
    public Game game;

    public ChangeGameSettings(Game game, GameSettings gameSettings){
        this.game = game;
        this.gameSettings = gameSettings;
    }

    public ChangeGameSettings(Game game){
        this.game = game;
        this.gameSettings = game.gameSettings;
    }

    public ChangeGameSettings(GameSettings gameSettings){
        this.gameSettings = gameSettings;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        int count = applySettingsList(args);
        sender.sendMessage("Applied "+ count +" settings");
        return count != 0;
    }

    int applySettingsList(String[] settingsList){
        int count = 0;
        for (String argument : settingsList){
            String[] setting = parseArgument(argument);
            if(setting != null && applySetting(setting[0], setting[1])) count++;
        }
        return count;
    }

    boolean applySetting(String name, String value){
        try {
            switch (name) {
                case "initial_size":
                    gameSettings.initialSize = validateNumber(value);
                    break;
                case "final_size":
                    gameSettings.finalSize = validateNumber(value);
                    break;
                case "total_duration":
                    gameSettings.totalDuration = validateNumber(value);
                    break;
                case "pact_duration":
                    gameSettings.pactDuration = validateNumber(value);
                    break;
                case "time_on_pre_game":
                    gameSettings.timeOnPreGame = validateNumber(value);
                    break;
                case "initial_time":
                    gameSettings.initialTime = validateNumber(value);
                    break;
                case "show_advancements":
                    gameSettings.showAdvancements = validateBoolean(value);
                    break;
                case "do_day_cycle_on_pre_game":
                    gameSettings.doDayCycleOnPreGame = validateBoolean(value);
                    break;
                case "do_day_cycle":
                    gameSettings.doDayCycle = validateBoolean(value);
                    break;
                case "pvp_on_pact":
                    gameSettings.pvpOnPact = validateBoolean(value);
                    break;
                case "messages_interval":
                    gameSettings.messagesInterval = validateNumber(value);
                    break;
                case "player_lives":
                    gameSettings.playerLives = validateNumber(value);
                    break;
                case "countdown":
                    gameSettings.countdown = validateNumber(value);
                    break;
                case "world":
                    World w = Bukkit.getWorld(value);
                    if(w == null) return false;
                    gameSettings.world = w;
                    break;
                default:
                    return false;
            }
        }catch (Exception e){
            return false;
        }
        return  true;
    }

    int validateNumber(String value) throws Exception {
        int val = Integer.parseInt(value);
        if(val < 1) throw new Exception("World size smaller than 1");
        if(val > 60000000) throw new Exception("World size bigger than 60000000");
        return  val;
    }

    boolean validateBoolean(String value) throws Exception {
        if(value.toLowerCase(Locale.ROOT).equals("false")){
            return false;
        }else
        if(value.toLowerCase(Locale.ROOT).equals("true")){
            return  true;
        }
        throw new Exception("Invalid boolean value");
    }

    public String[]  parseArgument(String argument){
        String[] pair = argument.split("=");
        if(pair.length == 2){
            return pair;
        }
        else return null;
    }

    void apply(){
        if(game != null){
            game.gameSettings = gameSettings;
            game.applySettings();
        }
    }
}




