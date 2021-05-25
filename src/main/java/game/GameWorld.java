package game;

import org.bukkit.*;
import org.bukkit.entity.Player;

public class GameWorld {
    Game game;
    GameWorld(Game game){
        this.game = game;
    }

    public World getWorld(){
        return this.game.gameSettings.world;
    }

    public void teleportToLobby(){
        for(Player player : Bukkit.getServer().getOnlinePlayers()){
            player.teleport(game.gameSettings.getLobbyLocation());
        }
    }

    public void setLobbyGameModes(){
        for(Player player : Bukkit.getServer().getOnlinePlayers()){
            player.setGameMode(GameMode.ADVENTURE);
        }
    }

    public void setLobbyBorder(){
        getWorld().getWorldBorder().setCenter(0,0);
        getWorld().getWorldBorder().setSize(10);
    }

    public void setLobbyPlatform(){
        World world = getWorld();
        for (int x = -5; x < 5; x++){
            for (int z = -5; z < 5; z++){
                int y = 240;
                world.getBlockAt(x,y,z).setType(Material.GLASS);
                for (y = 241; y < 255; y++){
                    world.getBlockAt(x,y,z).setType(Material.AIR);
                }
            }
        }

    }

    public static String locationToText(Location location){
        return "Mundo: "+location.getWorld().getName()+", X: "+Math.round(location.getX())+", Y: "+Math.round(location.getY())+", Z: "+Math.round(location.getZ());
    }

    public void breakLobbyPlatform(){
        World world = getWorld();
        for (int x = -5; x < 5; x++){
            for (int z = -5; z < 5; z++){
                int y = 240;
                world.getBlockAt(x,y,z).setType(Material.AIR);
            }
        }

    }
}
