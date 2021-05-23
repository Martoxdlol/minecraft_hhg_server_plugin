package game;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class GameStatus {
    Game game;
    List<GameEvent> events = new ArrayList<GameEvent>();

    GameStatus(Game game){
        this.game = game;
    }

    List<GamePlayer> alivePlayers(){
        List<GamePlayer> players = new ArrayList<GamePlayer>();
        for(GamePlayer player : game.getPlayers()){
            if(player.isAlive()){
                players.add(player);
            }
        }
        return players;
    }

    public void pushEvent(GameEvent event){
        event.tick = game.getTickNum();
        events.add(event);
    }

    public List<GameEvent> getEvents() {
        return events;
    }

    public void showLastEvent(){
        Bukkit.broadcastMessage(events.get(events.size() -1).getMessage());
    }
}

