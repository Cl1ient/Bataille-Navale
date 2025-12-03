import Controller.GameController;
import Model.Coordinate;
import Model.EntityType;
import Model.Game.Game;
import Model.Game.GameConfiguration;
import View.ConfigView;
import View.StartMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main( String[] args ) {
        System.out.println("Main");
        /*
                    Map<EntityType, List<Coordinate>> placement = new HashMap<>();
                    placement.put(EntityType.CRUISER, new ArrayList<>(List.of(new Coordinate(1,2), new Coordinate(1,3), new Coordinate(1,4), new Coordinate(1,5))));
                    placement.put(EntityType.DESTROYER, new ArrayList<>(List.of(new Coordinate(4,6), new Coordinate(5,6), new Coordinate(6,6))));
                    GameConfiguration config = new GameConfiguration(10, placement, false, "Valentin");

                    Game game = new Game(config);
         */

        GameController game = new GameController();
        ConfigView startMenu = new ConfigView(game);
        startMenu.showScreen();



    }
}
