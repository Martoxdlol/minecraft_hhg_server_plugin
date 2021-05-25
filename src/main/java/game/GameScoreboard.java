package game;

import commands.ShowGameStatus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class GameScoreboard {
    Game game;
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    Scoreboard board = manager.getNewScoreboard();
    Objective statusBoard;
    int len = 0;

    GameScoreboard(Game game){
        this.game = game;
    }

    public void updateStatusList(){
        len = 999;
        if(statusBoard != null){
            statusBoard.unregister();
        }
        statusBoard = board.registerNewObjective("status", "dummy", ChatColor.GOLD+""+ChatColor.BOLD+"Huerto Hunger Games");
        statusBoard.setDisplaySlot(DisplaySlot.SIDEBAR);

        String totalTeams = ChatColor.GREEN+""+game.getNonEmptyTeams().size()+ChatColor.RESET;
        String totalPlayers = ChatColor.GREEN+""+game.getPlayers().size()+ChatColor.RESET;
        String alivePlayers = ChatColor.GREEN+""+game.status.alivePlayers().size()+ChatColor.RESET;
        String aliveTeams = ChatColor.GREEN+""+game.status.teamsAlive().size()+ChatColor.RESET;
        appendItem(ChatColor.AQUA+"Jugadores:"+ChatColor.RESET+" total "+totalPlayers+" vivos "+alivePlayers);
        if(!totalTeams.equals(totalPlayers)){
            appendItem(ChatColor.AQUA+"Equipos:"+ChatColor.RESET+": total "+totalTeams+" vivos "+aliveTeams);
        }
        appendItem(game.getTimeLeftMessage());
        /*appendItem(" ");
        appendItem("--------------------------------------");
        appendItem((ChatColor.AQUA)+("EVENTOS DEL JUEGO") + (ChatColor.RESET));
        appendItem("-------------------------------------");
        for(GameEvent event : game.status.getEvents()){
            appendItem(event.getMessage());
        }*/
        appendItem("  ");
        appendItem(ChatColor.RED+"kills "+(ChatColor.GOLD)+""+(ChatColor.BOLD)+("JUGADORES DEL JUEGO: "),777);
        for(GamePlayer player : game.getPlayers()){
            int kills = player.getKills();
            appendItem(ChatColor.RED+" "+kills+ChatColor.RESET+" - "+player.player.getDisplayName(),kills);
        }
        appendItem("+info "+ChatColor.AQUA+"/game_status"+ChatColor.RESET+" y "+ChatColor.AQUA+"/show_players",-1);
    }

    void appendItem(String item){
        appendItem(item, len, true);
        len--;
    }

    void appendItem(String item, int scoreNum){
        appendItem(item, scoreNum, false);
    }

    void appendItem(String item, int scoreNum, boolean useLen){
        if(item.length() > 40){
            appendItem(item.substring(0,39),scoreNum,useLen);
            if (useLen) len--;
            appendItem(ChatColor.getLastColors(item.substring(0,39))+item.substring(39),scoreNum-1,useLen);
            return;
        }
        Score score = statusBoard.getScore(item);
        score.setScore(scoreNum);
    }

    public void applyToPlayers(){
        for(Player player : Bukkit.getServer().getOnlinePlayers()){
            player.setScoreboard(board);
        }
    }

    public void setTeamsToPlayer(GamePlayer player){
        Team team = board.registerNewTeam(player.player.getName());
        team.setColor(player.team.color);
        team.addEntry(player.player.getName());
        team.setPrefix("");
        team.setSuffix("");
        team.setDisplayName(player.team.getDisplayName());
    }
}
