package controller;

import model.Coordinate;
import model.EntityType;
import model.boat.Boat;
import model.player.ComputerPlayer;
import model.player.HumanPlayer;
import model.player.Player;
import model.weapon.Weapon;
import model.game.Game;
import model.game.GameConfiguration;
import model.trap.Trap;
import view.ConfigView;
import view.GameView;
import view.PlacementView;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class GameController {

    private Game game;
    private GameConfiguration gameConfig;
    private Map<EntityType, Integer> boatsToPlace;

    private String currentWeaponMode = "MISSILE";

    private final ConfigView configView;
    private PlacementView placementView;
    private GameView gameView;

    public GameController() {
        this.currentWeaponMode = "MISSILE";
        this.configView = new ConfigView(this);
        this.configView.showScreen();
    }

    public void setGameConfig(GameConfiguration config) {
        this.gameConfig = config;
    }

    public GameConfiguration getGameConfiguration() {
        return this.gameConfig;
    }

    public void setBoatsToPlace(Map<EntityType, Integer> boatsToPlace) {
        this.boatsToPlace = boatsToPlace;
    }

    public void setWeaponMode(String mode) {
        this.currentWeaponMode = mode;
        System.out.println("Arme sélectionnée : " + mode);
    }

    public void callPlaceEntityView() {
        if (this.configView != null) this.configView.dispose();
        this.placementView = new PlacementView(this, gameConfig.getGridSize(), boatsToPlace);
        this.placementView.showScreen();
    }

    public void startGame(Map<EntityType, List<Coordinate>> humanPlacement) {
        this.game = new Game(this.gameConfig);

        HumanPlayer hp = (HumanPlayer) this.game.getHumanPlayer();
        ComputerPlayer cp = this.game.getM_computerPlayer();

        hp.placeEntity(humanPlacement);
        cp.placeRandomEntities(this.boatsToPlace);

        hp.updateTotalShipSegments();
        cp.updateTotalShipSegments();

        if (this.placementView != null) this.placementView.dispose();

        this.gameView = new GameView(this, game);
        this.gameView.showScreen();
    }

    public void handleHumanAttack(int x, int y) {
        Coordinate target = new Coordinate(x, y);
        HumanPlayer hp = (HumanPlayer) this.game.getHumanPlayer();

        Weapon currentWeapon = hp.getWeapon(this.currentWeaponMode);

        if (currentWeapon == null || currentWeapon.getUsesLeft() == 0) {
            gameView.setStatus("ERREUR : L'arme " + this.currentWeaponMode + " est épuisée !");
            return;
        }

        if (this.currentWeaponMode.equals("SONAR")) {
            if (!canUseSonar(hp)) {
                gameView.setStatus("Impossible : Votre Sous-marin est détruit !");
                return;
            }
        }

        if (currentWeapon.isOffensive() && game.getM_computerPlayer().getOwnGrid().isAlreadyHit(x, y)) {
            gameView.setStatus("Cible déjà touchée ! Choisissez-en une autre.");
            return;
        }

        gameView.setStatus("Tir (" + this.currentWeaponMode + ") en cours...");
        game.processAttack(hp, currentWeapon, target);

        triggerComputerTurn();
    }

    public void handlePlaceTrap(Trap trap, Coordinate coord) {
        Player player = game.getHumanPlayer();
        player.placeNewTrap(trap, coord);
        gameView.updateGrids();
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
    }

    private boolean canUseSonar(Player player) {
        for (Boat boat : player.getOwnGrid().getOwnBoats()) {
            if (boat.getType() == EntityType.SUBMARINE) {
                return !boat.isSunk();
            }
        }
        return false;
    }
}