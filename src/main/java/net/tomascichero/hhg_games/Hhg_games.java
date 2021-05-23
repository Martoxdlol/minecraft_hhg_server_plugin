package net.tomascichero.hhg_games;

import commands.ChangeGameSettings;
import commands.ShowSettings;
import commands.StartGameCommandExecutor;
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
        game.applySettingsPreGame();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
