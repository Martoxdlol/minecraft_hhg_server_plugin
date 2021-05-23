package game;

import org.bukkit.Location;
import org.bukkit.World;

public class GameSettings {
    public int totalDuration = 0;
    public int pactDuration = 0;
    public int initialSize = 0;
    public int finalSize = 0;
    public boolean pvpOnPact = false;
    public boolean showAdvancements = true;
    public boolean doDayCycleOnPreGame = false;
    public boolean doDayCycle = true;
    public boolean showDeathMessage = true;
    public boolean teamPvp = false;
    public int timeOnPreGame = 0;
    public int initialTime = 0;
    public int messagesInterval = 5; //Minutes
    public int playerLives = 1;
    public int countdown = 5;
    public World world;
    private Location lobbyLocation = new Location(world,0,242,0);
    public GameSettings(){

    }
    public GameSettings(int totalDuration, int pactDuration, int finalSize, int initialSize) {
        this.totalDuration = totalDuration;
        this.pactDuration = pactDuration;
        this.finalSize = finalSize;
        this.initialSize = initialSize;
    }

    public Location getLobbyLocation() {
        lobbyLocation.setWorld(world);
        return lobbyLocation;
    }
}
