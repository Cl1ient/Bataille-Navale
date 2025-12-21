package controller;

import model.Coordinate;
import model.EntityType;
import model.boat.Boat;
import model.game.Game;
import model.game.GameConfiguration;
import model.player.ComputerPlayer;
import model.player.HumanPlayer;
import model.player.Player;
import model.trap.Trap;
import model.weapon.Weapon;
import view.ConfigView;
import view.gameView.GameView;
import view.placementView.PlacementView;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class GameController {

    private Game m_game;
    private GameConfiguration m_gameConfig;
    private Map<EntityType, Integer> m_boatsToPlace;

    private String m_currentWeaponMode = "MISSILE";

    private Trap m_trapToPlace = null;

    private ConfigView m_configView;
    private PlacementView m_placementView;
    private GameView m_gameView;

    public GameController() {
        this.m_currentWeaponMode = "MISSILE";
        this.m_configView = new ConfigView(this);
        this.m_configView.showScreen();
    }

    public void setGameConfig(GameConfiguration config) {
        this.m_gameConfig = config;
    }

    public void setBoatsToPlace(Map<EntityType, Integer> boatsToPlace) {
        this.m_boatsToPlace = boatsToPlace;
    }

    public void setWeaponMode(String mode) {
        if (this.m_trapToPlace != null) {
            m_gameView.setStatus("Placement annulé. Mode arme : " + mode);
            this.m_trapToPlace = null;
        }
        this.m_currentWeaponMode = mode;
        System.out.println("Arme sélectionnée : " + mode);
    }

    public void callPlaceEntityView() {
        if (this.m_configView != null) this.m_configView.dispose();
        boolean islandMode = (m_gameConfig != null) && m_gameConfig.isIslandMode();
        this.m_placementView = new PlacementView(this, m_gameConfig.getGridSize(), m_boatsToPlace);
        this.m_placementView.showScreen();
    }

    public void startGame(Map<EntityType, List<Coordinate>> humanPlacement) {
        this.m_game = new Game(this.m_gameConfig);

        HumanPlayer hp = (HumanPlayer) this.m_game.getHumanPlayer();
        ComputerPlayer cp = this.m_game.getComputerPlayer();

        hp.placeEntity(humanPlacement);
        cp.placeRandomEntities(this.m_boatsToPlace);

        hp.updateTotalShipSegments();
        cp.updateTotalShipSegments();

        if (this.m_placementView != null) this.m_placementView.dispose();

        this.m_gameView = new GameView(this, m_game);
        this.m_gameView.showScreen();
    }

    public void handleHumanAttack(int x, int y) {
        Coordinate target = new Coordinate(x, y);
        HumanPlayer hp = (HumanPlayer) this.m_game.getHumanPlayer();

        if (this.m_trapToPlace != null) {
            handlePlaceTrap(this.m_trapToPlace, target);
            return;
        }
        Weapon currentWeapon = hp.getWeapon(this.m_currentWeaponMode);

        if (currentWeapon == null || currentWeapon.getUsesLeft() == 0) {
            m_gameView.setStatus("ERREUR : L'arme " + this.m_currentWeaponMode + " est épuisée !");
            return;
        }

        if (currentWeapon.isOffensive() && m_game.getComputerPlayer().getOwnGrid().isAlreadyHit(x, y)) {
            m_gameView.setStatus("Cible déjà touchée !");
            return;
        }

        if (this.m_currentWeaponMode.equals("SONAR")) {
            if (!canUseSonar(hp)) {
                m_gameView.setStatus("Impossible : Sous-marin détruit !");
                return;
            }
        }
        m_gameView.setStatus("Tir (" + this.m_currentWeaponMode + ") en cours...");
        m_game.processAttack(hp, currentWeapon, target);
        triggerComputerTurn();
    }

    public Boolean handlePlaceTrap(Trap trap, Coordinate coord) {
        if(trap == null) {
            trap = this.m_trapToPlace;
        }
        HumanPlayer hp = m_game.getHumanPlayer();
        boolean success = m_game.tryPlaceTrap(hp, trap, coord);
        this.m_trapToPlace = trap;
        if (success) {
            m_gameView.setStatus("Succès ! Piège " + trap.getType() + " placé en " + coord);
            m_gameView.updateGrids();

            this.m_trapToPlace = null;
            return false;
        } else {
            return true;
        }
    }

    private void triggerComputerTurn() {
        m_gameView.setInputEnabled(false);
        m_gameView.setStatus("L'adversaire réfléchit...");

        Timer timer = new Timer(10, e -> {
            playComputerTurn();
            ((Timer)e.getSource()).stop();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void playComputerTurn() {
        m_game.processComputerAttack();
    }

    private boolean canUseSonar(Player player) {
        for (Boat boat : player.getOwnGrid().getOwnBoats()) {
            if (boat.getType() == EntityType.SUBMARINE) {
                return !boat.isSunk();
            }
        }
        return false;
    }

    public GameConfiguration getGameConfiguration() {
        return m_gameConfig;
    }

    public void restartGame() {
        if (this.m_gameView != null) {
            this.m_gameView.dispose();
        }
        this.m_trapToPlace = null;
        this.m_currentWeaponMode = "MISSILE";

        this.m_configView = new ConfigView(this);
        this.m_configView.showScreen();

        System.out.println("[CONTROLLER] Redémarrage du jeu...");
    }
}