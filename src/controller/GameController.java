package controller;

import model.Coordinate;
import model.EntityType;
import model.game.Game;
import model.game.GameConfiguration;
import model.player.ComputerPlayer;
import model.player.HumanPlayer;
import model.weapon.Weapon;
import model.weapon.WeaponFactory;
import view.ConfigView;
import view.GameView;
import view.PlacementView;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class GameController {

    // --- MODÈLE ---
    private Game game;
    private GameConfiguration gameConfig;
    private Map<EntityType, Integer> boatsToPlace;
    private Weapon m_bombe;
    private Weapon m_missile;
    private Weapon m_sonar;

    private final ConfigView configView;
    private PlacementView placementView;
    private GameView gameView;
    private String currentWeaponMode = "MISSILE";
    public GameController() {
        this.configView = new ConfigView(this);
        this.configView.showScreen();
        this.m_bombe = new WeaponFactory().createBomb();
        this.m_missile = new WeaponFactory().createMissile();
        this.m_sonar = new WeaponFactory().createSonar();
    }

    public void setGameConfig(GameConfiguration config) {
        this.gameConfig = config;
    }

    public void setBoatsToPlace(Map<EntityType, Integer> boatsToPlace) {
        this.boatsToPlace = boatsToPlace;
    }

    public void setWeaponMode(String mode) {
        this.currentWeaponMode = mode;
        System.out.println("Arme changée : " + mode);
    }

    public void callPlaceEntityView() {
        if (this.configView != null) this.configView.dispose();

        System.out.println("[CONTROLLER] Ouverture du placement...");
        this.placementView = new PlacementView(this, gameConfig.getGridSize(), boatsToPlace);
        this.placementView.showScreen();
    }

    public void startGame(Map<EntityType, List<Coordinate>> humanPlacement) {
        System.out.println("[CONTROLLER] Initialisation du jeu...");

        this.game = new Game(this.gameConfig);

        HumanPlayer hp = (HumanPlayer) this.game.getHumanPlayer();
        ComputerPlayer cp = this.game.getM_computerPlayer();

        hp.placeEntity(humanPlacement);

        cp.placeRandomEntities(this.boatsToPlace);

        if (this.placementView != null) this.placementView.dispose();

        this.gameView = new GameView(this, game);
        this.gameView.showScreen();

        System.out.println("[CONTROLLER] Jeu démarré !");
    }

    public void handleHumanAttack(int x, int y) {
        Coordinate target = new Coordinate(x, y);
        if (game.getM_computerPlayer().getOwnGrid().isAlreadyHit(x, y)) {
            return;
        }

        Weapon currentWeapon = m_missile;
        switch (this.currentWeaponMode) {
            case "BOMB":
                currentWeapon = m_bombe;
                break;
            case "SONAR":
                currentWeapon = m_sonar;
                break;
            case "MISSILE":
            default:
                currentWeapon = m_missile;
                break;
        }

        if (currentWeapon.getUsesLeft() == 0) {
            if (gameView != null) {
                gameView.setStatus("ERREUR : " + this.currentWeaponMode + " épuisé ! Choisissez une autre arme.");
            }
            return;
        }

        gameView.setStatus("Tir en cours...");
        game.processAttack(game.getHumanPlayer(), currentWeapon, target);
        gameView.updateGrids();
        if (checkVictory()) return;
        triggerComputerTurn();
    }

    private void triggerComputerTurn() {
        gameView.setInputEnabled(false);
        gameView.setStatus("L'adversaire réfléchit...");

        Timer timer = new Timer(1000, e -> {
            playComputerTurn();
            ((Timer)e.getSource()).stop();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void playComputerTurn() {
        game.processComputerAttack();
        gameView.updateGrids();
        if (checkVictory()) return;
        gameView.setInputEnabled(true);
        gameView.setStatus("À vous de jouer !");
    }

    private boolean checkVictory() {
        if (game.isGameOver()) {
            String winnerName = game.getWinner().getNickName();
            gameView.setStatus("PARTIE TERMINÉE ! Vainqueur : " + winnerName);
            gameView.setInputEnabled(false); // Bloque tout
            JOptionPane.showMessageDialog(gameView,
                    "La partie est terminée.\nLe vainqueur est : " + winnerName,
                    "Fin de Partie",
                    JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }
}