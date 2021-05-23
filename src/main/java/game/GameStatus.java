package game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameStatus {
    Game game;
    List<GameEvent> events = new ArrayList<GameEvent>();

    GameStatus(Game game){
        this.game = game;
    }

    public List<GamePlayer> alivePlayers(){
        List<GamePlayer> players = new ArrayList<GamePlayer>();
        for(GamePlayer player : game.getPlayers()){
            if(player.isAlive()){
                players.add(player);
            }
        }
        return players;
    }

    public List<GamePlayer> teamAliveMembers(GameTeam team){
        List<GamePlayer> players = new ArrayList<GamePlayer>();
        for(GamePlayer player : team.getMembers()){
            if(player.isAlive()){
                players.add(player);
            }
        }
        return players;
    }

    public List<GameTeam> teamsAlive(){
        List<GameTeam> teams = new ArrayList<GameTeam>();
        for(GameTeam team : game.getTeams()){
            if(!teamAliveMembers(team).isEmpty()){
                teams.add(team);
            }
        }
        return teams;
    }

    public void pushEvent(GameEvent event){
        event.tick = game.getTickNum();
        game.status.setPlayersDisplayNames();
        events.add(event);
    }

    public List<GameEvent> getEvents() {
        return events;
    }

    public void showLastEvent(){
        Bukkit.broadcastMessage(events.get(events.size() -1).getMessage());
    }




    public void setPlayersDisplayNames(){
        _setPlayersDisplayNames(Bukkit.getServer().getOnlinePlayers());
    }

    private void _setPlayersDisplayNames(Collection<? extends Player> players){
        for(Player player : players){
            GamePlayer gamePlayer = game.getPlayer(player);
            if(gamePlayer != null){
                if(gamePlayer.isAlive()) player.setDisplayName(ChatColor.GREEN+"[Vivo] "+player.getName()+ChatColor.RESET);
                else player.setDisplayName(ChatColor.RED+"[Muerto] "+player.getName()+player.getName()+ChatColor.RESET);
            }else{
                player.setDisplayName(ChatColor.DARK_GRAY+"[Espectador] "+player.getName()+player.getName()+ChatColor.RESET);
            }
        }
    }

    public boolean winnerAction(){
        List<GameTeam> aliveTeams = teamsAlive();
        if(aliveTeams.size() == 1){
            GameTeam winnerTeam = aliveTeams.get(0);
            game.status.pushEvent(new GameEvent(GameEvent.TEAM_WIN, winnerTeam));
            game.status.showLastEvent();
            List<GamePlayer> winners = teamAliveMembers(winnerTeam);
            for(GamePlayer winner : winners){
                game.status.pushEvent(new GameEvent(GameEvent.PLAYER_WIN, winner));
                game.status.showLastEvent();
            }
            return true;
        }else if(aliveTeams.size() == 0){
            return true;
        }
        return false;
    }
}

