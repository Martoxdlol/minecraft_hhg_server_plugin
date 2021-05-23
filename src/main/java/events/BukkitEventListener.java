package events;

import game.Game;
import game.GamePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class BukkitEventListener implements Listener {
    Game game;

    public BukkitEventListener(Game game){
        this.game = game;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        System.out.println(event.getPlayer().getName()+" JOINED");
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
