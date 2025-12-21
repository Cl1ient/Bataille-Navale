package model.game;

import model.*;
import model.boat.Boat;
import model.boat.BoatFactory;
import model.trap.*;
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
    private boolean m_gameFinished = false;

    private HumanPlayer m_humanPlayer;
    private ComputerPlayer m_computerPlayer;
    private Player m_currentPlayer;

    private Weapon m_currentWeaponUsed;
    private GameConfiguration m_game;
    private int m_turnNumber;
    private final List<GameListener> m_listeners;
    private List<IslandListener> m_islandListeners = new ArrayList<>();

    private StringBuilder m_historyLog;

    public Game(GameConfiguration config) {

        System.out.println("[DEBUG] Initialisation du jeu…");

        this.m_game = config;
        this.m_humanPlayer = new HumanPlayer(config);
        this.m_computerPlayer = new ComputerPlayer(config);

        this.m_trapFactory = new TrapFactory();
        this.m_listeners = new ArrayList<>();

        this.m_humanPlayer.setMediator(this);
        this.m_computerPlayer.setMediator(this);

        this.m_currentPlayer = m_humanPlayer;
        this.m_turnNumber = 1;
        boolean isIsland = m_game.isIslandMode();

        /// ////////////////////////////////// POTENTIELLEMENT A SUPPRIMER ///////////////////////////////////////////////////////////////////////////////////////////////

        m_humanPlayer.getOwnGrid().setIslandMod(isIsland);
        m_computerPlayer.getOwnGrid().setIslandMod(isIsland);

        /// ////////////////////////////////// POTENTIELLEMENT A SUPPRIMER ///////////////////////////////////////////////////////////////////////////////////////////////


        if (isIsland) {
            m_humanPlayer.getOwnGrid().initIslandItems();
            m_computerPlayer.getOwnGrid().initIslandItems();
            m_humanPlayer.getWeapon("BOMB").use();
            m_humanPlayer.getWeapon("SONAR").use();
            m_computerPlayer.getWeapon("SONAR").use();
            m_computerPlayer.getWeapon("BOMB").use();
        }
        this.m_gameFinished = false;
        this.m_historyLog = new StringBuilder();
        this.m_historyLog.append("=== DÉBUT DE LA PARTIE ===\n\n");
    }

    public String getHistory() {
        return m_historyLog.toString();
    }

    private void addToHistory(String action) {
        this.m_historyLog.append("[Tour ").append(m_turnNumber).append("] ").append(action).append("\n");
    }

    public void processComputerAttack() {

        if (isGameOver()) {
            System.out.println("[DEBUG] Impossible : Partie déjà terminée.");
            return;
        }
        Coordinate target = m_computerPlayer.choseCoord(m_humanPlayer.getOwnGrid());
        Weapon weapon = m_computerPlayer.choseWeapon();

        processAttack(m_computerPlayer, weapon, target);

        incrementTurnNumber();

        if (!checkGameOver()) {
            nextTurn();
        }
    }

    public void processAttack(Player attacker, Weapon weapon, Coordinate coord) {
        Player defender = getOpponent(attacker);
        Coordinate targetCoord = coord;
        if (m_game.isIslandMode() && defender.getOwnGrid().coordIsinIsland(coord)) {
            processIslandSearch(attacker, defender, coord);
            weapon.use();
            return;
        }
        if (attacker.isUnderTornadoInfluence() && weapon.isOffensive()) {
            targetCoord = generateRandomCoordinate(attacker.getGridSize());
            attacker.decrementTornadoEffect();
            addToHistory("🌪️ TORNADE : Le tir de " + attacker.getNickName() + " est dévié vers " + targetCoord + " !");
        }

        displayGridPlayer();
        this.m_currentWeaponUsed = weapon;
        int gridSize = this.m_game.getGridSize();
        addToHistory(attacker.getNickName() + " utilise " + weapon.getName().toLowerCase() + " en " + targetCoord + "C'est là !!");
        if (weapon.isOffensive() && defender.getTypeEntityAt(targetCoord) == EntityType.BLACK_HOLE) {
            System.out.println("[DEBUG] → Tir DIRECT (Centre) sur BlackHole en " + targetCoord);

            defender.setHitCellAt(targetCoord.getX(), targetCoord.getY(), true);
            for (GameListener li : m_listeners) {
                li.onCellUpdated(defender, targetCoord);
                li.onBlackHolHit(attacker);
            }
            handleBlackHoleHit(defender, targetCoord);
            processAttack(defender, weapon, targetCoord);

            weapon.use();
            return;
        }
        List<Coordinate> targets = weapon.generateTargets(targetCoord, gridSize);
        if (weapon.isOffensive()) {
            if(m_game.isIslandMode() && weapon.getType() == EntityType.BOMB){
                List<Coordinate> validTargets = new ArrayList<>();
                for(Coordinate target : targets){
                    if (!defender.getOwnGrid().isPosOnIsland(target.getX(), target.getY())) {
                        validTargets.add(target);
                    }
                }
                targets = validTargets;
            }
            processOffensiveAttack(attacker, defender, targets);
        } else {
            processScan(attacker, defender, targets);
        }
        weapon.use();
    }


    private Coordinate generateRandomCoordinate(int gridSize) {
        java.util.Random rand = new java.util.Random();
        return new Coordinate(rand.nextInt(gridSize), rand.nextInt(gridSize));
    }

    private void processOffensiveAttack(Player attacker, Player defender, List<Coordinate> targets) {
        for (Coordinate t : targets) {
            EntityType type = defender.getTypeEntityAt(t);
            if (type == EntityType.BLACK_HOLE) {
                System.out.println("[DEBUG] → BlackHole touché par le BORD en " + t);
                defender.setHitCellAt(t.getX(), t.getY(), true);
                for (GameListener li : m_listeners) {
                    li.onCellUpdated(defender, t);
                    li.onBlackHolHit(attacker);
                }
                handleBlackHoleHit(defender, t);
                processShot(defender, attacker, t.getX(), t.getY());

                continue;
            }
            if (type == EntityType.STORM) {
                for (GameListener li : m_listeners) {
                    li.onCellUpdated(defender, t);
                    li.onStormHit(attacker);
                }

            }
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
            attacker.setLastMove(new Coordinate(x,y));
        } else {
            handleHit(defender, new Coordinate(x, y));
            attacker.setLastMove(new Coordinate(x,y));
            handleMiss(defender, x, y);
        }
    }

    private void processScan(Player attacker, Player defender, List<Coordinate> targets) {
        List<ScanResult> results = new ArrayList<>();
        int foundCount = 0;

        for (Coordinate target : targets) {
            if (!defender.getOwnGrid().isInside(target)) {
                continue;
            }
            Cell cell = defender.getCellAt(target.getX(), target.getY());
            EntityType TypeOfGridEntityFromCoord = defender.getTypeOfGridEntityFromCoord(target.getX(), target.getY());

            if (cell != null && cell.isFilled() && !defender.getOwnGrid().isPosOnIsland(target.getX(), target.getY())) {
                foundCount++;
                results.add(new ScanResult(target, TypeOfGridEntityFromCoord));
            }
        }

        addToHistory(" -> Sonar utilisé en " + targets.get(0) + " : " + foundCount + " entités détectées.");

        for (GameListener li : m_listeners){
            li.onScanResult(attacker, targets, results);
        }
    }

    public boolean isGameOver() {
        boolean over = m_humanPlayer.hasLost() || m_computerPlayer.hasLost();
        return over;
    }

    private boolean checkGameOver() {
        if (m_gameFinished) {
            return true;
        }
        if (isGameOver()) {
            m_gameFinished = true;

            Player winner = getWinner();
            addToHistory("=== FIN DE PARTIE : Victoire de " + winner.getNickName() + " ===");
            for (GameListener li : m_listeners) {
                li.onGameOver(winner);
            }
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
    public void addIslandListener(IslandListener listener) {
        this.m_islandListeners.add(listener);
    }

    public void handleMiss(Player defender, int x, int y) {
        System.out.println("[HANDLE] handleMiss(" + x + "," + y + ")");
        addToHistory(" -> Tir dans l'eau en (" + x + "," + y + ")");
        defender.getOwnGrid().markMiss(x, y);
        for (GameListener li : m_listeners) {
            li.onCellUpdated(defender, new Coordinate(x, y));
        }
    }

    @Override
    public void handleHit(Player defender, Coordinate coord) {
        addToHistory(" -> Tire effectué en " + coord + " !");
        for (GameListener li : m_listeners)
            li.onCellUpdated(defender, coord);
    }

    @Override
    public void handleBlackHoleHit(Player defender, Coordinate coord) {
        System.out.println("[HANDLE] handleBlackHoleHit() : BlackHole touché à " + coord);
        addToHistory(" -> TROU NOIR activé en " + coord);
    }

    @Override
    public void handleShipSunk(Player defender, Boat boat) {
        addToHistory(" -> BATEAU COULÉ (" + defender.getNickName() + ") !");
        for (GameListener li : m_listeners)
            li.onShipSunk(defender);
        checkGameOver();
    }

    public HumanPlayer getHumanPlayer(){
        return this.m_humanPlayer;
    }

    public ComputerPlayer getComputerPlayer(){
        return this.m_computerPlayer;
    }

    public void incrementTurnNumber() {
        this.m_turnNumber++;
    }

    public int getTurnNumber() {
        return m_turnNumber;
    }

    private void processIslandSearch(Player attacker, Player defender, Coordinate coord) {

        Cell cell = defender.getCellAt(coord.getX(), coord.getY());

        if (cell.isHit()) {
            addToHistory(attacker.getNickName() + " a déjà fouillé l'île en " + coord);
            return;
        }

        cell.setHit(true);
        GridEntity item = cell.getEntity();

        if (item != null) {
            addToHistory("TRÉSOR ! " + attacker.getNickName() + " trouve : " + item.getType());

            switch (item.getType()) {
                case NEW_BOMB:
                case NEW_SONAR:
                    attacker.addFoundItem(item.getType());
                    notifyWeaponFound(item.getType());
                    break;

                case NEW_STORM:

                    Trap storm = (Trap) m_trapFactory.createStorm(false);
                    notifyPlaceTrapRequest(storm, attacker);
                    break;

                case NEW_BLACKHOLE:
                    Trap blackHole = (Trap) m_trapFactory.createBlackHole(false);
                    notifyPlaceTrapRequest(blackHole, attacker);
                    break;

                default:
                    break;
            }

        } else {
            addToHistory(attacker.getNickName() + " fouille en " + coord + " mais ne trouve rien.");
        }

        for (GameListener li : m_listeners) {
            li.onCellUpdated(defender, coord);
        }
    }

    private void notifyPlaceTrapRequest(Trap trap, Player player) {
        for (IslandListener li : m_islandListeners) {
            li.notifyPlaceIslandEntity(trap, player);
        }
    }

    private void notifyWeaponFound(EntityType type) {
        for (IslandListener li : m_islandListeners) {
            li.notifyWeaponFind(type);
        }
    }

    @Override
    public void notifyTrapPlacementError() {
        for (IslandListener li : m_islandListeners) {
            li.notifyTrapWrongPlacement();
        }
    }

    public boolean tryPlaceTrap(Player player, Trap trap, Coordinate coord) {
        boolean success = player.placeFoundTrap(trap, coord, player.getOwnGrid());

        if (success) {
            return true;
        } else {
            notifyTrapPlacementError();
            return false;
        }
    }
}