import model.Coordinate;
import model.EntityType;
import model.ScanResult;
import model.game.Game;
import model.game.GameConfiguration;
import model.GameListener;
import model.player.ComputerPlayer;
import model.player.HumanPlayer;
import model.player.Player;
import model.weapon.Missile;
import model.weapon.Weapon;

import javax.swing.*;
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

        SwingUtilities.invokeLater(() -> {

            System.out.println("--- Démarrage du Test d'Architecture (FIN DE PARTIE) ---");

            final int GRID_SIZE = 6;
            final String PLAYER_NAME = "Capitaine Fin";
            Weapon missile = new Missile();

            Map<EntityType, List<Coordinate>> humanPlacement = new HashMap<>();
            humanPlacement.put(EntityType.SUBMARINE, List.of(
                    new Coordinate(1,1), new Coordinate(1,2), new Coordinate(1,3)
            ));

            Map<EntityType, List<Coordinate>> computerPlacement = new HashMap<>();
            computerPlacement.put(EntityType.SUBMARINE, List.of(
                    new Coordinate(2,0), new Coordinate(2,1), new Coordinate(2,2)
            ));

            GameConfiguration config = new GameConfiguration(
                    GRID_SIZE,
                    humanPlacement,
                    false,
                    PLAYER_NAME
            );

            HumanPlayer humanPlayer = new HumanPlayer(config);
            ComputerPlayer computerPlayer = new ComputerPlayer(config);

            Game game = new Game(config, humanPlayer, computerPlayer);
            game.addListener(new TestGameListener());

            game.placeComputerEntities(computerPlacement);

            System.out.println("\n--- Grilles après placement ---");
            game.displayGridPlayer();

            System.out.println("\n--- DÉBUT DE LA PARTIE ---");

            System.out.println("\n[TOUR 1 - Humain] : Segments 1 et 2");
            game.processAttack(humanPlayer, missile, new Coordinate(2,0));
            game.processAttack(humanPlayer, missile, new Coordinate(2,1));
            game.processComputerAttack(); // Ordinateur joue


            System.out.println("\n[TOUR 2 - Humain] : Coup FATAL (Segment 3)");
            game.processAttack(humanPlayer, missile, new Coordinate(2,2));
            if (!game.isGameOver()) game.processComputerAttack();

            System.out.println("\n--- FIN DE LA PARTIE ---");

            if (game.isGameOver()) {
                System.out.println("*** Statut final : L'Humain a gagné! ***");
            } else {
                System.out.println("*** Statut final : Partie non terminée (erreur) ***");
            }
        });
    }
}
