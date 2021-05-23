package game;

import org.bukkit.entity.Player;

import java.util.UUID;

public class GamePlayer {
    public Player player;
    Game game;
    public GameTeam team;

    private int lives;

    public int kills = 0;

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

    public boolean isAlive(){
        return lives > 0;
    }

    public void removeLive(int removeQuantity){
        if(lives - removeQuantity > 0 && removeQuantity > 0){
            lives -= removeQuantity;
        }
    }

    public void kill(){

    }

    public UUID getUniqueId(){
        return player.getUniqueId();
    }

    public void addLives(int lives) {
        this.lives += lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives() {
        return lives;
    }

    Player getPlayer(){
        return player;
    }
}
