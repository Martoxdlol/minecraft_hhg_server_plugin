package game;

import commands.ChangeGameSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static final int secondsPerMinute = 60;
    private static final int ticksPerSecond = 20;
    public GameSettings gameSettings;
    private int actualTime = -1;
    private int tickNum = -20 * 5 - 1;
    private int gameState = 0;
    private List<GamePlayer> players = new ArrayList<GamePlayer>();
    private List<GameTeam> teams = new ArrayList<GameTeam>();

    public Game(GameSettings gameSettings){
        this.gameSettings = gameSettings;
    }

    void start(){
        gameSettings.world.setTime(gameSettings.initialTime);
        applySettingsInGame();
    }

    void startLoop(){
        new BukkitRunnable(){
            @Override
            public void run(){
                tick();
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("HuertoHungerGames"), 0L, 1L);
    }

    void tick(){

    }

    public GamePlayer addPlayer(Player player){
        if(player == null) return null;
        if(getPlayer(player) != null) return null;
        GamePlayer newPlayer = new GamePlayer(player, this);
        String teamName = "[Player] " + newPlayer.player.getName();
        GameTeam newTeam = addTeam(teamName);
        if(newTeam == null) newTeam = getTeam(teamName);
        newPlayer.team = newTeam;
        players.add(newPlayer);
        return newPlayer;
    }

    public GameTeam addTeam(String teamName){
        if(getTeam(teamName) != null) return null;
        GameTeam newTeam = new GameTeam(this);
        newTeam.name = teamName;
        teams.add(newTeam);
        return newTeam;
    }

    public void addPlayerToTeam(GamePlayer player, GameTeam team){
        if(team == getTeam(team.name)) player.team = team;
    }

    public GameTeam getTeam(String teamName){
        for(GameTeam team : teams){
            if(team.name.equals(teamName)) return team;
        }
        return null;
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }

    public GamePlayer getPlayer(String playerName){
        Player player = Bukkit.getServer().getPlayer(playerName);
        return getPlayer(player);
    }

    public GamePlayer getPlayer(Entity e){
        if(e instanceof Player){
            return getPlayer((Player)e);
        }
        return null;
    }

    public GamePlayer getPlayer(Player player){
        for(GamePlayer gamePlayer : players){
            if(gamePlayer.player.getUniqueId() == player.getUniqueId()){
                return gamePlayer;
            }
        }
        return null;
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

    public List<GameTeam> getTeams() {
        return teams;
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
