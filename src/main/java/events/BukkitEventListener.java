package events;

import game.Game;
import game.GamePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class BukkitEventListener implements Listener {
    Game game;

    public BukkitEventListener(Game game){
        this.game = game;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {

    }

    @EventHandler
    public void onTestEntityDamage(EntityDamageByEntityEvent event)
    {
        Entity e = event.getEntity();
        Entity d = event.getDamager();
        GamePlayer player = game.getPlayer(e);
        GamePlayer damager = game.getPlayer(d);

        if(player != null){
            //Player playing
            if(damager != null && player.getUniqueId() != damager.getUniqueId()){
                if(game.inPact() && !game.gameSettings.pvpOnPact && (!game.gameSettings.teamPvp || damager.team != player.team)){
                    event.setCancelled(true);
                }else if(damager.team == player.team && !game.gameSettings.teamPvp){
                    event.setCancelled(true);
                }
            }
        }else if(e instanceof Player){
            //Player not playing
            if(!game.inGame()){
                event.setCancelled(true);
            }
        }
    }
}
