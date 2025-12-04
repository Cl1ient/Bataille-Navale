package model.game;

import model.*;
import model.boat.Boat;
import model.boat.BoatFactory;
import model.map.Cell;
import model.map.Grid;
import model.weapon.Weapon;
import model.player.HumanPlayer;
import model.player.ComputerPlayer;
import model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game implements GameMediator{
    private Player m_humanPlayer;
    private ComputerPlayer m_computerPlayer;
    private Player m_currentPlayer;
    private BoatFactory m_boatFactory;

    private final List<GameListener> m_listeners;

    public Game(GameConfiguration config){

        this.m_humanPlayer = new HumanPlayer(config);
        this.m_computerPlayer = new ComputerPlayer(config);
        this.m_currentPlayer = m_humanPlayer;
        this.m_boatFactory = new BoatFactory();
        this.m_listeners = new ArrayList<>();
        this.displayGridPlayer();

        this.m_humanPlayer.setMediator(this);
        this.m_computerPlayer.setMediator(this);
    }

    @Override
    public void handleHit(Player defender, Coordinate coord) {
        for (GameListener listener : m_listeners) {
            listener.onCellUpdated(defender, coord);
        }
    }

    @Override
    public void handleShipSunk(Player defender, Boat sunkBoat) {
        for (GameListener listener : m_listeners) {
            listener.onShipSunk(defender);
        }
        this.checkGameOver();
    }
    public void placeEntity(Map<EntityType, List<Coordinate>> entityPositions){
        this.m_humanPlayer.placeEntity(entityPositions);
    }

    public void processComputerAttack(){
        // choisie les Coord et Weapon de manière random
        Coordinate targetCoord = this.m_computerPlayer.choseCoord();
        Weapon weapon = this.m_computerPlayer.choseWeapon();

        this.processAttack(this.m_computerPlayer, weapon, targetCoord);
        this.checkGameOver();
        // TODO peut être implémenté un this.nextTurn();
    }

    public void processAttack(Player attacker, Weapon weapon, Coordinate coord){
        Player defender = getOpponent(attacker);

        List<Coordinate> targets = weapon.generateTargets(coord);
        // TODO revoir le scan
        if (weapon.isOffensive()) {
            processOffensiveAttack(attacker, defender, targets);
        } else {
            processScan(attacker, defender, targets);
        }
    }

    public void processOffensiveAttack(Player attacker, Player defender, List<Coordinate> targets){
        for(Coordinate target : targets){
            this.processShot(attacker, defender, target.getX(), target.getY());
        }
        this.checkGameOver();
        // TODO notifier l'observer
    }

    public void processShot(Player attacker, Player defender, int x, int y){
        Cell targetCell = defender.getTargetCell(x, y);
        if(targetCell.getEntity() != null){
            defender.receiveShot(new Coordinate(x, y), attacker);
        }else{
            this.handleMiss(defender,x,y);
        }
        //TODO notifier l'observer
    }

    // TODO Revoir cette méthode
    public void processScan(Player attacker, Player defender, List<Coordinate> targets){
        List<ScanResult> results = new ArrayList<>();

        for(GameListener listener : m_listeners){
            listener.onScanResult(attacker, results);
        }
    }

    public void handleMiss(Player defender, int x, int y) {
        defender.getOwnGrid().markMiss(x, y);
    }

    public boolean isGameOver() {
        return m_humanPlayer.hasLost() || m_computerPlayer.hasLost();
    }

    public Player getWinner(){
        if (m_humanPlayer.hasLost()){
            return m_computerPlayer;
        }
        return m_humanPlayer;
    }

    public void addListener(GameListener listener){
        this.m_listeners.add(listener);
    }

    public void removeListener(GameListener listener){
        this.m_listeners.remove(listener);
    }

    private void checkGameOver(){
        if(isGameOver()){
            Player winner = getWinner();
            for(GameListener listener : m_listeners){
                listener.onGameOver(winner);
            }
        }
    }

    public Player getOpponent(Player p) {
        return (p == this.m_humanPlayer) ? this.m_computerPlayer : this.m_humanPlayer;
    }

    public void displayGridPlayer(){
        System.out.println("grille de : " + this.m_humanPlayer.getNickName());
        this.m_humanPlayer.getOwnGrid().displayGrid();

        System.out.println("grille de : " + this.m_computerPlayer.getNickName());
        this.m_computerPlayer.getOwnGrid().displayGrid();
    }
}