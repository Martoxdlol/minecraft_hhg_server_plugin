package events;

import game.Game;
import game.GameEvent;
import game.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;

public class BukkitEventListener implements Listener {
    Game game;

    public BukkitEventListener(Game game){
        this.game = game;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        System.out.println(player.getName()+" JOINED");

        if(game.inGame()){
            GamePlayer gamePlayer = game.getPlayer(player);
            System.out.println("IN GAME");
            System.out.println(gamePlayer);
            System.out.println(gamePlayer.isAlive());

            if(gamePlayer != null && gamePlayer.isAlive()){

            }else {
                player.setGameMode(GameMode.SPECTATOR);
            }
        }else {
            player.teleport(game.gameSettings.getLobbyLocation());
            player.setGameMode(GameMode.ADVENTURE);
        }
        game.status.setPlayersDisplayNames();
        //Collection<String> tabMenu = new ArrayList<String>();
        //tabMenu.add(ChatColor.GOLD+"Huerto Hunger Games - official server");
        //event.setJoinMessage("");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity().getPlayer();
        Player killer = player.getKiller();
        GamePlayer gamePlayer = game.getPlayer(player);
        GamePlayer gameKiller = game.getPlayer(killer);
        EntityDamageEvent damageCause = player.getLastDamageCause();
        Biome biome = player.getLocation().getBlock().getBiome();
        if(gameKiller != null && gamePlayer != null && gameKiller.isAlive()){
            //damageCause.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK);
            gameKiller.kills += 1;
            game.status.pushEvent(new GameEvent(GameEvent.PLAYER_KILL, gamePlayer, gameKiller));
            game.status.showLastEvent();
        }
        if(gamePlayer != null){
            gamePlayer.lastDeathLocation = gamePlayer.player.getLocation();
            gamePlayer.lastInventoryContent = gamePlayer.player.getInventory().getContents();
            gamePlayer.lastXp = gamePlayer.player.getExp();
            gamePlayer.killOnce();
            if(!gamePlayer.isAlive()){
                if(gameKiller != null){
                    game.status.pushEvent(new GameEvent(GameEvent.PLAYER_DIED, gamePlayer, gameKiller, biome));
                }else {
                    game.status.pushEvent(new GameEvent(GameEvent.PLAYER_DIED, gamePlayer, damageCause.getCause(), biome));
                }
                game.status.showLastEvent();
            }else{
                gamePlayer.removeLive(1);
            }
        }
        game.status.setPlayersDisplayNames();
        game.checkWin();
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        GamePlayer gamePlayer = game.getPlayer(player);
        if(gamePlayer != null){
            gamePlayer.player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 150 + 5 * 20 +1, 10));
            gamePlayer.player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 150 + 4 * 20 +1, 10));
            if(game.inGame() && !gamePlayer.isAlive()){
                gamePlayer.player.setGameMode(GameMode.SPECTATOR);
            }
        }
    }

    @EventHandler
    public void onTestEntityDamageByDamage(EntityDamageEvent event){
        Entity e = event.getEntity();
        boolean isEntityDamage = event instanceof EntityDamageByEntityEvent;
        Entity d = null;
        if(isEntityDamage){
            d = ((EntityDamageByEntityEvent) event).getDamager();
        }
        GamePlayer player = game.getPlayer(e);
        GamePlayer damager = game.getPlayer(d);

        if(player != null){
            //Player in game
            if(damager != null && player.getUniqueId() != damager.getUniqueId()){
                if(game.inPact() && !game.gameSettings.pvpOnPact && (!game.gameSettings.teamPvp || damager.team != player.team)){
                    event.setCancelled(true);
                }else if(damager.team == player.team && !game.gameSettings.teamPvp){
                    event.setCancelled(true);
                }
            }
            if(!game.inGame()){
                event.setCancelled(true);
            }
        }else if(e instanceof Player){
            //Player not in game (espectator)
            if(!game.inGame()){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onTestEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        onTestEntityDamageByDamage(event);
    }
}
