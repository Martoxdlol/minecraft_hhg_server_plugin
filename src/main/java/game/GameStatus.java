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

    public int getPlayerKills(GamePlayer player){
        int count = 0;
        for(GameEvent event : events){
            if(event.type.equals(GameEvent.PLAYER_KILL) && event.affectivePlayer.player.getName().equals(player.player.getName())){
                count++;
            }
        }
        return count;
    }

    public void pushEvent(GameEvent event){
        event.tick = game.getTickNum();
        game.status.setPlayersDisplayNames();
        events.add(event);
        game.gameScoreboard.updateStatusList();
    }

    public List<GameEvent> getEvents() {
        return events;
    }

    public void showLastEvent(){
        Bukkit.broadcastMessage(events.get(events.size() -1).getMessage());
    }




    public void setPlayersDisplayNames(){
        for(Player player : Bukkit.getServer().getOnlinePlayers()){
            if(game.getPlayer(player) == null){
                if(game.inGame()){
                    player.setDisplayName(ChatColor.GRAY+"[Espectador] "+player.getName()+ChatColor.RESET);
                }else {
                    player.setDisplayName(ChatColor.DARK_PURPLE+""+player.getName()+ChatColor.RESET);
                }
            }
        }
        for(GamePlayer gamePlayer : game.getPlayers()){
            Player player = gamePlayer.player;
            if(game.inGame()){
                if(gamePlayer.isAlive()) player.setDisplayName(ChatColor.GREEN+"[Vivo] "+player.getName()+ChatColor.RESET);
                else player.setDisplayName(ChatColor.RED+"[Muerto] "+player.getName()+ChatColor.RESET);
            }else {
                player.setDisplayName(ChatColor.AQUA+"[Player] "+player.getName()+ChatColor.RESET);
            }
        }
        game.gameScoreboard.updateStatusList();
        game.gameScoreboard.applyToPlayers();
    }

    public GameEvent getLastPlayerDeath(){
        for (int i = events.size()-1; i >= 0; i--){
            if(events.get(i).type.equals(GameEvent.PLAYER_DIED)){
                return events.get(i);
            }
        }
        return null;
    }

    public boolean winnerAction(){
        if(game.inEnd()) return false;
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

    public void resetLastDeathEvents(GamePlayer player){
        int i = events.size()-1;
        boolean killRemoved = false;
        boolean deathRemoved = false;
        while (i >= 0){
            GameEvent event = events.get(i);
            if(!deathRemoved && event.type.equals(GameEvent.PLAYER_DIED) && event.affectedPlayer.player.getName().equals(player.player.getName())){
                events.remove(i);
                deathRemoved = true;
            }else if(!killRemoved && event.type.equals(GameEvent.PLAYER_KILL) && event.affectedPlayer.player.getName().equals(player.player.getName())){
                events.remove(i);
                killRemoved = true;
            }else{
                i--;
            }
            if (killRemoved && deathRemoved){
                break;
            }
        }
    }

    public void removeEndEvents(){
        int i = 0;
        while (i < events.size()){
            GameEvent event = events.get(i);
            if(event.type.equals(GameEvent.PLAYER_WIN) || event.type.equals(GameEvent.TEAM_WIN) || event.type.equals(GameEvent.GAME_ENDED)){
                events.remove(i);
            }else {
                i++;
            }
        }
    }
}

