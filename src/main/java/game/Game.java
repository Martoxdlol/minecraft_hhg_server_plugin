package game;

import commands.ChangeGameSettings;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.WorldBorder;

public class Game {
    private static final int secondsPerMinute = 60;
    private static final int ticksPerSecond = 20;
    public GameSettings gameSettings;
    private int actualTime = -1;
    private int tickNum = -20 * 5 - 1;
    private int gameState = 0;

    public Game(GameSettings gameSettings){
        this.gameSettings = gameSettings;
    }

    void start(){
        gameSettings.world.setTime(gameSettings.initialTime);
        applySettingsInGame();
    }

    public boolean inGame(){
        return gameState > 0;
    }

    public boolean inPact(){
        return gameState == 1;
    }

    public boolean inPve(){
        return gameState == 2;
    }

    public boolean inDeathMatch(){
        return gameState == 3;
    }

    public boolean inEnd(){
        return gameState == 4;
    }

    public void applySettings(){
        if(inGame()){
            applySettingsInGame();
        }else{
            applySettingsPreGame();
        }
    }

    public void applySettingsPreGame(){
        gameSettings.world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, gameSettings.showAdvancements);
        gameSettings.world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, gameSettings.showDeathMessage);
        gameSettings.world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, gameSettings.doDayCycleOnPreGame);
        gameSettings.world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
        gameSettings.world.setTime(gameSettings.timeOnPreGame);
        gameSettings.world.setTime(gameSettings.timeOnPreGame);
    }

    public void applySettingsInGame(){
        gameSettings.world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, gameSettings.showAdvancements);
        gameSettings.world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, gameSettings.showDeathMessage);
        gameSettings.world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, gameSettings.doDayCycle);
        gameSettings.world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
        WorldBorder worldBorder = gameSettings.world.getWorldBorder();
        worldBorder.setCenter(0, 0);
        if(!gameSettings.doDayCycle){
            gameSettings.world.setTime(gameSettings.initialTime);
        }
        if(!inDeathMatch()){
            worldBorder.setSize(gameSettings.initialSize);
        }else {
            worldBorder.setSize(gameSettings.finalSize);
        }
    }
}
