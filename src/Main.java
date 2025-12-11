import model.Coordinate;
import model.EntityType;
import model.ScanResult;
import model.game.Game;
import model.game.GameConfiguration;
import model.GameListener;
import model.player.ComputerPlayer;
import model.player.HumanPlayer;
import model.player.Player;
import model.weapon.Bombe;
import model.weapon.Missile;
import model.weapon.Sonar;
import model.weapon.Weapon;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TestGameListener implements GameListener {
    @Override public void turnEnded(Player a, Map<Coordinate, String> s) { System.out.println("-> [LISTENER]: Tour terminé."); }
    @Override public void onCellUpdated(Player p, Coordinate c) { System.out.println("-> [LISTENER]: Cellule mise à jour: " + c); }
    @Override public void onShipSunk(Player p) { System.out.println("-> [LISTENER]: BATEAU COULÉ! par " + p.getNickName()); }
    @Override public void onGameOver(Player w) { System.out.println("-> [LISTENER]: *** JEU TERMINÉ. Vainqueur: " + w.getNickName() + " ***"); }
    @Override public void onScanResult(Player player, List<ScanResult> results) { System.out.println("-> [LISTENER]: Sonar détecté."); }
}

public class Main {

    public static void main(String[] args) {

       System.out.println("Main");

        Map<EntityType, List<Coordinate>> placement = new HashMap<>();
        placement.put(EntityType.CRUISER, new ArrayList<>(List.of(new Coordinate(1,2), new Coordinate(1,3), new Coordinate(1,4), new Coordinate(1,5))));
        placement.put(EntityType.BLACK_HOLE, new ArrayList<>(List.of(new Coordinate(4,2))));
        placement.put(EntityType.BLACK_HOLE, new ArrayList<>(List.of(new Coordinate(6,6))));
        GameConfiguration config = new GameConfiguration(10, placement, false, "Valentin");

        Game game = new Game(config);
        game.addListener(new TestGameListener());

        Weapon missile = new Bombe();
        //game.processAttack(game.getM_computerPlayer(), missile, new Coordinate(4,2));
        game.processAttack(game.getM_computerPlayer(), missile, new Coordinate(6,6));
        game.displayGridPlayer();

        //game.processAttack(computer, missile, new Coordinate(1,3));
        //game.processAttack(computer, missile, new Coordinate(1,4));
        //game.processAttack(computer, missile, new Coordinate(1,5));
        //game.processComputerAttack(); // Ordinateur joue
    }
}
