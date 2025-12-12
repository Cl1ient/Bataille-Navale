package model.game;

import model.*;
import model.boat.Boat;
import model.boat.BoatFactory;
import model.entity.trap.BlackHole;
import model.entity.trap.TrapFactory;
import model.map.Cell;
import model.player.HumanPlayer;
import model.player.ComputerPlayer;
import model.player.Player;
import model.weapon.Weapon;
import model.weapon.WeaponFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Game implements GameMediator {

    private final TrapFactory m_trapFactory;
    private final WeaponFactory m_weaponFactory;
    private final BoatFactory m_boatFactory;

    private Player m_humanPlayer;
    private ComputerPlayer m_computerPlayer;
    private Player m_currentPlayer;

    private Weapon m_currentWeaponUsed;
    private GameConfiguration m_game;
    private int turnNumber;
    private final List<GameListener> m_listeners;

    public Game(GameConfiguration config) {

        System.out.println("[DEBUG] Initialisation du jeu…");

        this.m_game = config;
        this.m_humanPlayer = new HumanPlayer(config);
        this.m_computerPlayer = new ComputerPlayer(config);

        this.m_trapFactory = new TrapFactory();
        this.m_weaponFactory = new WeaponFactory();
        this.m_boatFactory = new BoatFactory();
        this.m_listeners = new ArrayList<>();

        this.m_humanPlayer.setMediator(this);
        this.m_computerPlayer.setMediator(this);

        this.m_currentPlayer = m_humanPlayer;
        this.turnNumber = 1;
        displayGridPlayer();
    }

    public void processComputerAttack() {

        if (isGameOver()) {
            System.out.println("[DEBUG] Impossible : Partie déjà terminée.");
            return;
        }
        Coordinate target = m_computerPlayer.choseCoord();
        Weapon weapon = m_computerPlayer.choseWeapon();
        processAttack(m_computerPlayer, weapon, target);
        if (!checkGameOver()) {
            nextTurn();
        }
    }

    public void processAttack(Player attacker, Weapon weapon, Coordinate coord) {
        this.m_currentWeaponUsed = weapon;
        Player defender = getOpponent(attacker);
        List<Coordinate> targets = weapon.generateTargets(coord);
        if (weapon.isOffensive()) {
            processOffensiveAttack(attacker, defender, targets);
        } else {
            processScan(attacker, defender, targets);
        }
        weapon.use();
    }

    private void processOffensiveAttack(Player attacker, Player defender, List<Coordinate> targets) {
        for (Coordinate t : targets) {
            if (defender.getTypeEntityAt(t) == EntityType.BLACK_HOLE) {
                System.out.println("Je suis la");
                System.out.println("[DEBUG] → BlackHole détecté sur " + t);
                processAttack(defender, m_currentWeaponUsed, t);
                return;
            }
        }
        System.out.println("Je suis toujours la");
        for (Coordinate t : targets) {
            processShot(attacker, defender, t.getX(), t.getY());
            if (isGameOver()) {
                checkGameOver();
                return;
            }
        }
        checkGameOver();
    }

    private void processShot(Player attacker, Player defender, int x, int y) {
        Cell cell = defender.getTargetCell(x, y);
        if (cell == null) {
            return;
        }
        if (cell.getEntity() != null) {
            defender.receiveShot(new Coordinate(x, y), attacker);
        } else {
            handleHit(defender, new Coordinate(x, y));
            handleMiss(defender, x, y);
        }
    }

    private void processScan(Player attacker, Player defender, List<Coordinate> targets) {
        // TODO j'ai pas encore fait
        List<ScanResult> results = new ArrayList<>();
        for (GameListener li : m_listeners){
            li.onScanResult(attacker, results);
        }
    }

    public boolean isGameOver() {
        boolean over = m_humanPlayer.hasLost() || m_computerPlayer.hasLost();
        return over;
    }

    private boolean checkGameOver() {
        if (isGameOver()) {
            Player winner = getWinner();
            for (GameListener li : m_listeners)
                li.onGameOver(winner);
            return true;
        }
        return false;
    }

    public Player getWinner() {
        return (m_humanPlayer.hasLost()) ? m_computerPlayer : m_humanPlayer;
    }

    public Player getOpponent(Player p) {
        Player opp = (p == m_humanPlayer) ? m_computerPlayer : m_humanPlayer;
        System.out.println("[DEBUG] getOpponent(" + p.getNickName() + ") -> " + opp.getNickName());
        return opp;
    }

    public void nextTurn() {
        System.out.println("[DEBUG] nextTurn()");

        if (isGameOver()) {
            System.out.println("[DEBUG] nextTurn() ignoré → Partie terminée");
            return;
        }

        if (m_currentPlayer == m_humanPlayer)
            m_currentPlayer = m_computerPlayer;
        else
            m_currentPlayer = m_humanPlayer;

        System.out.println("[DEBUG] Nouveau joueur courant : " + m_currentPlayer.getNickName());
    }

    public void displayGridPlayer() {
        System.out.println("grille de : " + m_humanPlayer.getNickName());
        m_humanPlayer.getOwnGrid().displayGrid();

        System.out.println("grille de : " + m_computerPlayer.getNickName());
        m_computerPlayer.getOwnGrid().displayGrid();
    }

    public void addListener(GameListener listener){ this.m_listeners.add(listener); }


    // HANDLER

    public void handleMiss(Player defender, int x, int y) {
        System.out.println("[HANDLE] handleMiss(" + x + "," + y + ")");
        defender.getOwnGrid().markMiss(x, y);
    }

    @Override
    public void handleHit(Player defender, Coordinate coord) {
        System.out.println("hit");
        for (GameListener li : m_listeners)
            li.onCellUpdated(defender, coord);
    }

    @Override
    public void handleBlackHoleHit(Player defender, Coordinate coord) {
        System.out.println("[HANDLE] handleBlackHoleHit() : BlackHole touché à " + coord);
    }

    @Override
    public void handleShipSunk(Player defender, Boat boat) {
        for (GameListener li : m_listeners)
            li.onShipSunk(defender);
        checkGameOver();
    }

    public Player getHumanPlayer(){
        return this.m_humanPlayer;
    }

    public ComputerPlayer getM_computerPlayer(){
        return this.m_computerPlayer;
    }
    public void incrementTurnNumber() {
        this.turnNumber++;
    }
    public int getTurnNumber() {
        return turnNumber;
    }
}