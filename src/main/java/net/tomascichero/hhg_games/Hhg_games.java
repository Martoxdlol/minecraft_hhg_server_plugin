package net.tomascichero.hhg_games;

import commands.*;
import events.BukkitEventListener;
import game.Game;
import game.GameSettings;
import org.bukkit.Bukkit;
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
        this.getCommand("add_player").setExecutor(new AddPlayer(game));
        getServer().getPluginManager().registerEvents(new BukkitEventListener(game), this);
        game.applySettingsPreGame();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
