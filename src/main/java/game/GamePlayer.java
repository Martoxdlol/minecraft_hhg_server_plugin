package game;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class GamePlayer {
    public Player player;
    Game game;
    public GameTeam team;

    private int lives = 1;

    public int kills = 0;

    public ItemStack[] lastInventoryContent;

    public float lastXp;

    public Location lastDeathLocation;

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

    public int getKills(){
        return game.status.getPlayerKills(this);
    }

    public boolean isAlive(){
        return lives > 0;
    }

    public void removeLive(int removeQuantity){
        if(lives - removeQuantity > 0 && removeQuantity > 0){
            lives -= removeQuantity;
        }
    }

    public void killOnce(){
        lives--;
        if(lives == 0){
            kill();
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
