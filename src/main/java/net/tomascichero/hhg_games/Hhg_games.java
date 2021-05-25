package net.tomascichero.hhg_games;

import commands.*;
import events.BukkitEventListener;
import game.Game;
import game.GameSettings;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Hhg_games extends JavaPlugin {
    public GameSettings gameSettings = new GameSettings();
    public Game game = new Game(gameSettings);

    @Override
    public void onEnable() {
        getLogger().info("HHG PLUGIN ENABLED");
        gameSettings.world = Bukkit.getWorld("world");
        this.getCommand("start_game").setExecutor(new StartGameCommandExecutor(game));
        this.getCommand("apply_settings").setExecutor(new ChangeGameSettings(game, gameSettings));
        this.getCommand("show_settings").setExecutor(new ShowSettings(gameSettings));
        this.getCommand("add_team").setExecutor(new AddTeam(game));
        this.getCommand("set_team").setExecutor(new SetTeam(game));
        this.getCommand("team_color").setExecutor(new TeamColor(game));
        this.getCommand("show_teams").setExecutor(new ShowTeams(game));
        this.getCommand("show_players").setExecutor(new ShowPlayers(game));
        this.getCommand("add_player").setExecutor(new AddPlayer(game));
        this.getCommand("game_lobby").setExecutor(new LobbyCommand(game.getGameWorld()));
        this.getCommand("game_status").setExecutor(new ShowGameStatus(game));
        this.getCommand("reset_game").setExecutor(new ResetGame(game));
        this.getCommand("revive").setExecutor(new Revive(game));
        this.getCommand("un_finish").setExecutor(new UnFinish(game));
        getServer().getPluginManager().registerEvents(new BukkitEventListener(game), this);
        game.applySettingsPreGame();
        game.getGameWorld().setLobbyBorder();
        game.getGameWorld().setLobbyPlatform();
        game.gameScoreboard.updateStatusList();
        //game.getGameWorld().teleportToLobby();
    }

    private GameSettings clone(GameSettings gameSettings) {
        GameSettings newGameSettings = new GameSettings();
        newGameSettings.initialTime = gameSettings.initialTime;
        newGameSettings.timeOnPreGame = gameSettings.timeOnPreGame;
        newGameSettings.finalSize = gameSettings.finalSize;
        newGameSettings.pactDuration = gameSettings.initialTime;
        newGameSettings.world = gameSettings.world;
        newGameSettings.countdown = gameSettings.countdown;
        newGameSettings.playerLives = gameSettings.playerLives;
        newGameSettings.totalDuration = gameSettings.totalDuration;
        newGameSettings.teamPvp = gameSettings.teamPvp;
        newGameSettings.messagesInterval = gameSettings.messagesInterval;
        newGameSettings.showDeathMessage = gameSettings.showDeathMessage;
        newGameSettings.pvpOnPact = gameSettings.pvpOnPact;
        newGameSettings.doDayCycle = gameSettings.doDayCycle;
        newGameSettings.doDayCycleOnPreGame = gameSettings.doDayCycleOnPreGame;
        return newGameSettings;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
