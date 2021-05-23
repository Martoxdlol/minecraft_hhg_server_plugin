package game;

import java.util.ArrayList;
import java.util.List;

public class GameStatus {
    Game game;
    List<GameEvent> events = new ArrayList<GameEvent>();

    GameStatus(Game game){
        this.game = game;
    }
}

