package game;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class GameTeam {
    public Game game;
    public ChatColor color = ChatColor.GOLD;
    public String name;
    public GameTeam(Game game){
        this.game = game;
    }

    public List<GamePlayer> getMembers(){
        List<GamePlayer> list = new ArrayList<GamePlayer>();
        for(GamePlayer player : game.getPlayers()){
            if(player.team == this){
                list.add(player);
            }
        }
        return list;
    }

    public String getDisplayName(){
        return color+name+ChatColor.RESET;
    }
}
