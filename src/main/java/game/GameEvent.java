package game;

import org.bukkit.ChatColor;
import game.GamePlayer;
import org.bukkit.block.Biome;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Map;

public class GameEvent {
    GamePlayer affectedPlayer;
    GamePlayer affectivePlayer;
    GameTeam affectedTeam;
    Biome biome;
    int tick = 0;
    EntityDamageEvent.DamageCause affectiveDamageCause;
    public String type = "";

    public GameEvent(String eventName, GamePlayer affected, GamePlayer affective){
        type = eventName;
        affectedPlayer = affected;
        affectivePlayer = affective;
    }

    public GameEvent(String eventName, GamePlayer affected, GamePlayer affective, Biome biome){
        type = eventName;
        affectedPlayer = affected;
        affectivePlayer = affective;
        this.biome = biome;
    }

    public GameEvent(String eventName, GamePlayer affected){
        type = eventName;
        affectedPlayer = affected;
    }

    public GameEvent(String eventName, GameTeam affected){
        type = eventName;
        affectedTeam = affected;
    }


    public GameEvent(String eventName, GamePlayer affected, EntityDamageEvent.DamageCause affective){
        type = eventName;
        affectedPlayer = affected;
        affectiveDamageCause = affective;
    }

    public GameEvent(String eventName, GamePlayer affected, EntityDamageEvent.DamageCause affective, Biome biome){
        type = eventName;
        affectedPlayer = affected;
        affectiveDamageCause = affective;
        this.biome = biome;
    }

    GameEvent(String eventName){
        type = eventName;
    }

    String getAffectedName(){
        if(affectedPlayer != null) return affectedPlayer.player.getDisplayName();
        if(affectedTeam != null) return affectedTeam.color+affectedTeam.name;
        return "";
    }

    String getAffectiveName(){
        if(affectivePlayer != null) return affectedPlayer.player.getDisplayName();
        if(affectiveDamageCause != null) return ChatColor.RED+affectiveDamageCause.name();
        return "";
    }

    String getBiomeName(){
        if(biome == null) return ChatColor.GREEN+"[Overworld]"+ChatColor.RESET;
        return biome.name();
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    String getTime(){
        return GameEvent.getTime(tick);
    }

    public String getMessage(){
        String message = GameEvent.eventsStrings.get(type);
        String extra = "";
        if(message == null) return ChatColor.AQUA+"";
        if(affectiveDamageCause != null && affectivePlayer == null && type == GameEvent.PLAYER_DIED){
            extra = ". Muerto por "+ChatColor.RED+affectiveDamageCause.name();
        }
        message = ChatColor.AQUA+"["+getTime()+"] "+ChatColor.RESET+message;
        message = message.replace("$affective", ChatColor.GOLD+getAffectiveName()+ChatColor.AQUA);
        message = message.replace("$affected", ChatColor.GOLD+getAffectedName()+ChatColor.AQUA);
        message = message.replace("$biome", ChatColor.GREEN+getBiomeName()+ChatColor.AQUA);
        message = message.replace("$extra", extra+ChatColor.AQUA);
        return message;
    }

    public static String PLAYER_JOINED = "PLAYER_JOINED";
    public static String GAME_STARTED = "GAME_STARTED";
    public static String PACT_ENDED = "PACT_ENDED";
    public static String DEATHMATCH_STARTED = "DEATHMATCH_STARTED";
    public static String GAME_ENDED = "GAME_ENDED";
    public static String PLAYER_DIED = "PLAYER_DIED";
    public static String PLAYER_KILL = "PLAYER_KILL";
    public static String TEAM_WIN = "TEAM_WIN";
    public static String PLAYER_WIN = "PLAYER_WIN";
    static Map<String, String> eventsStrings = Map.of(
            GameEvent.PLAYER_JOINED, "¡El jugador $affected se unió al juego!",
            GameEvent.GAME_STARTED, "¡El juego ha comenzado!",
            GameEvent.PACT_ENDED, "¡El pacto ha terminado!",
            GameEvent.DEATHMATCH_STARTED, "¡El deathmatch a comenzado!",
            GameEvent.GAME_ENDED, "¡El juego a terminado!",
            GameEvent.PLAYER_DIED, "¡Murió el jugador $affected, en bioma $biome$extra!",
            GameEvent.PLAYER_KILL, "¡El jugador $affective mató a $affected!",
            GameEvent.TEAM_WIN, "¡El equipo $affected ganó la partida!",
            GameEvent.PLAYER_WIN, "¡El jugador $affected ganó la partida!"
    );

    public static String getTime(int tick){
        int hour = (int) tick / (Game.ticksPerSecond * Game.secondsPerMinute * Game.minutesPerHour);
        int minutes = ((int) tick / (Game.ticksPerSecond * Game.secondsPerMinute)) % Game.minutesPerHour;
        int seconds = ((int) tick / Game.ticksPerSecond) % (Game.minutesPerHour * Game.secondsPerMinute);
        return ""+hour+":"+minutes+":"+seconds+"";
    }
}
