package game;

import org.bukkit.entity.Player;

import java.util.UUID;

public class GamePlayer {
    public Player player;
    Game game;
    public GameTeam team;
    private boolean alive = true;

    public GamePlayer(Game game, Player player){
        constructor(player, game);
    }

    public GamePlayer(Player player, Game game){
        constructor(player, game);
    }

    public void constructor(Player player, Game game){
        this.player = player;
        this.game = game;
    }

    boolean isAlive(){
        return alive;
    }

    public void setKilled(){
        alive = false;
    }

    public UUID getUniqueId(){
        return player.getUniqueId();
    }

    Player getPlayer(){
        return player;
    }
}
