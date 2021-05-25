package game;

import commands.ChangeGameSettings;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game {
    public static final int secondsPerMinute = 60;
    public static final int minutesPerHour = 60;
    public static final int ticksPerSecond = 20;
    public GameSettings gameSettings;
    private int actualTime = -1;
    private int tickNum = 0;
    private int gameState = 0;
    private List<GamePlayer> players = new ArrayList<GamePlayer>();
    private List<GameTeam> teams = new ArrayList<GameTeam>();
    public GameStatus status = new GameStatus(this);
    private GameWorld gameWorld;
    public GameScoreboard gameScoreboard = new GameScoreboard(this);
    BukkitTask loop;
    public Game(GameSettings gameSettings){
        this.gameSettings = gameSettings;
        this.gameWorld = new GameWorld(this);
    }

    public int getGameState() {
        return gameState;
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public void start(){
        tickNum = -gameSettings.countdown * ticksPerSecond;
        startLoop();
        System.out.println("INICIANDO JUEGO");
        Bukkit.broadcastMessage("El juego va a comenzar!");
        for(GamePlayer player : players){
            player.setLives(gameSettings.playerLives);
        }
    }

    void startLoop(){
        loop = new BukkitRunnable(){
            @Override
            public void run(){
                if(!inEnd()){
                    testSetStateLoop();
                }
                tick();
                tickNum++;
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("HuertoHungerGames"), 0L, 1L);
    }

    public void testSetStateLoop(){
        if(tickNum == 0){
            gameState = 1;
            gameStarted();
        }else
        if(isMinuteChange() && getMinute() == gameSettings.pactDuration){
            gameState = 2;
            pactEnded();
        }else
        if(isMinuteChange() && getMinute() == gameSettings.totalDuration){
            gameState = 3;
            deathMatchStarted();
        }
        if(isMinuteChange()){
            status.setPlayersDisplayNames();
        }
    }

    public void testSetState(){
        if(getMinute() < gameSettings.pactDuration){
            gameState = 1;
        }else
        if(getMinute() >= gameSettings.pactDuration){
            gameState = 2;
            pactEnded();
        }else
        if(getMinute() >= gameSettings.totalDuration){
            gameState = 3;
            deathMatchStarted();
        }
    }

    void tick(){
        if(isMinuteChange() && (getMinute() % gameSettings.messagesInterval == 0 || getMinute() >= gameSettings.totalDuration-5 && getMinute() <= gameSettings.totalDuration)){
            sendTimeLeftMessage();
            gameScoreboard.updateStatusList();
        }else if(tickNum < 0 && isSecondChange()){
            sendTimeLeftMessage();
        }
        if(isMinuteChange()){
            gameScoreboard.updateStatusList();
        }
    }

    void gameStarted(){
        Bukkit.broadcastMessage(ChatColor.GOLD+"Arrancó el juego!");
        applySettingsInGame();
        gameStartedPlayerEffects();
        gameSettings.world.setStorm(false);
        gameSettings.world.setTime(gameSettings.initialTime);
        status.pushEvent(new GameEvent(GameEvent.GAME_STARTED));
        gameWorld.breakLobbyPlatform();
    }

    void pactEnded(){
        Bukkit.broadcastMessage(ChatColor.GOLD+"Se terminó el pacto!");
        applySettingsInGame();
        status.pushEvent(new GameEvent(GameEvent.PACT_ENDED));
    }

    void deathMatchStarted(){
        status.pushEvent(new GameEvent(GameEvent.DEATHMATCH_STARTED));
        Bukkit.broadcastMessage(ChatColor.GOLD+"Arrancó el deathmatch!");
        applySettingsInGame();
    }

    void gameEnded(){
        Bukkit.broadcastMessage(ChatColor.GREEN+"Terminó la partida");
    }

    public void checkWin(){
        if(status.winnerAction()){
            gameState = 4;
            loop.cancel();
            gameEnded();
        }
    }

    boolean isMinuteChange(){
        return tickNum % (ticksPerSecond * secondsPerMinute) == 0;
    }

    boolean isSecondChange(){
        return tickNum % ticksPerSecond == 0;
    }

    void gameStartedPlayerEffects(){
        for(GamePlayer player : players){
            applyPlayerEffects(player);
        }
    }

    public void stop(){
        if(loop != null){
            loop.cancel();
        }
        gameState = 4;
    }

    public boolean unFinish(){
        if(!inEnd()) return false;
        if(players.size() < 2) return false;
        if(loop != null && !loop.isCancelled()) loop.cancel();
        status.removeEndEvents();
        status.setPlayersDisplayNames();
        startLoop();
        applySettingsInGame();
        testSetState();
        return true;
    }

    public void applyPlayerEffects(GamePlayer player){
        player.player.setGameMode(GameMode.SURVIVAL);
        player.player.getInventory().clear();
        //Clear potion effects
        for(PotionEffect effect : player.player.getActivePotionEffects())
        {
            player.player.removePotionEffect(effect.getType());
        }
        player.player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 150 + 5 * 20 +1, 10));
        player.player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 150, 10));
        player.player.setHealth(20);
        player.player.setExp(0);
        //Clear advancements
        Iterator<Advancement> iterator = Bukkit.getServer().advancementIterator();
        while (iterator.hasNext())
        {
            AdvancementProgress progress = player.player.getAdvancementProgress(iterator.next());
            for (String criteria : progress.getAwardedCriteria())
                progress.revokeCriteria(criteria);
        }
    }

    int getSecond(){
        return tickNum / ticksPerSecond;
    }

    String getTimeLeftMessage(){
        int time = 0;
        if(!inGame()){
            time = -getSecond();
            return(ChatColor.GOLD+"El juego empieza en "+ChatColor.AQUA+time+ChatColor.GOLD+" segundos!");
        }
        if(inPact()) {
            time = gameSettings.pactDuration - getMinute();
            return(ChatColor.GOLD+"Quedan "+ChatColor.AQUA+time+ChatColor.GOLD+" minutos de pacto!");
        }
        else if(inPve()) {
            time = gameSettings.totalDuration - getMinute();
            return(ChatColor.GOLD+"Quedan "+ChatColor.AQUA+time+ChatColor.GOLD+" minutos antes del deathmatch!");
        }
        else if(inDeathMatch()) {
            time = getMinute() - gameSettings.totalDuration;
            return(ChatColor.GOLD+"Van "+ChatColor.AQUA+time+ChatColor.GOLD+" minutos del deathmatch!");
        }else if(inEnd()){
            return ChatColor.BLUE+"El juego finalizó";
        }
        return ChatColor.BLUE+"Esperando para iniciar";
    }

    void sendTimeLeftMessage(){
        int time = 0;
        Bukkit.broadcastMessage(getTimeLeftMessage());
    }

    int getMinute(){
        return getSecond() / secondsPerMinute;
    }

    public GamePlayer addPlayer(Player player){
        if(player == null) return null;
        if(getPlayer(player) != null) return null;
        GamePlayer newPlayer = new GamePlayer(player, this);
        String teamName = "[Player] " + newPlayer.player.getName();
        GameTeam newTeam = addTeam(teamName);
        if(newTeam == null) newTeam = getTeam(teamName);
        newPlayer.team = newTeam;
        newPlayer.setLives(gameSettings.playerLives);
        players.add(newPlayer);
        status.setPlayersDisplayNames();
        GameEvent joinedEvent = new GameEvent(GameEvent.PLAYER_JOINED, newPlayer);
        status.pushEvent(joinedEvent);
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
        if(e == null) return null;
        if(e instanceof Player){
            return getPlayer((Player)e);
        }
        return null;
    }

    public GamePlayer getPlayer(Player player){
        if(player == null) return null;
        for(GamePlayer gamePlayer : players){
            if(gamePlayer.player.getName().equals(player.getName())){
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

    public int getTickNum() {
        return tickNum;
    }

    public List<GameTeam> getTeams() {
        return teams;
    }

    public List<GameTeam> getNonEmptyTeams() {
        List<GameTeam> list = new ArrayList<GameTeam>();
        for (GameTeam team : teams){
            if(!team.getMembers().isEmpty()) list.add(team);
        }
        return list;
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
        gameSettings.world.setDifficulty(Difficulty.HARD);
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

    public void reset(){
        stop();
        actualTime = -1;
        tickNum = 0;
        gameState = 0;
        players = new ArrayList<GamePlayer>();
        teams = new ArrayList<GameTeam>();
        status = new GameStatus(this);
        status.setPlayersDisplayNames();
    }
}
