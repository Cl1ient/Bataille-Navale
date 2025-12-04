package model.game;

import model.Coordinate;
import model.EntityType;
import model.GridEntity;
import model.boat.BoatFactory;
import model.map.Cell;
import model.map.Grid;
import model.weapon.Weapon;
import model.player.HumanPlayer;
import model.player.ComputerPlayer;
import model.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private Player m_humanPlayer;
    private ComputerPlayer m_computerPlayer;
    private Player m_currentPlayer;
    private BoatFactory m_boatFactory;

    public Game(GameConfiguration config){

        this.m_humanPlayer = new HumanPlayer(config);
        this.m_computerPlayer = new ComputerPlayer(config);
        this.m_currentPlayer = m_humanPlayer;
        this.m_boatFactory = new BoatFactory();
        this.displayGridPlayer();
    }


    public void placeEntity(Map<EntityType, List<Coordinate>> entityPositions){
        this.m_humanPlayer.placeEntity(entityPositions);
    }

    public void processComputerAttack(){
        // choisie les Coord et Weapon de manière random
        Coordinate targetCoord = this.m_computerPlayer.choseCoord();
        Weapon weapon = this.m_computerPlayer.choseWeapon();

        this.processAttack(this.m_computerPlayer, weapon, targetCoord);

        //this.checkGameOver();
        //this.nextTurn();

    }

    public void processAttack(Player attacker, Weapon weapon, Coordinate coord){
        Player defender = getOpponent(attacker);

        List<Coordinate> targets = weapon.generateTargets(coord);

        if (weapon.isOffensive()) {
            processOffensiveAttack(attacker, defender, targets);
        } else {
            processScan(attacker, defender, targets);
        }
    }

    public void processOffensiveAttack(Player attacker, Player defender, List<Coordinate> coord){
        Map<EntityType, List<Coordinate>> entityPositions = new HashMap<>();

        for(Coordinate coordinate : coord){
            this.processSh
        }
    }

    public void ProcessShot(Player attacker, Player defender, int x, int y){
        Cell targetCell = defender.getTargetCell(x, y);
        if(targetCell.getEntity() != null){
            targetCell.getEntity().onHit(attacker,defender,x,y);
        }
    }

    public void displayGridPlayer(){
        System.out.println("grille de : " + this.m_humanPlayer.getNickName());
        this.m_humanPlayer.getOwnGrid().displayGrid();

        System.out.println("grille de : " + this.m_computerPlayer.getNickName());
        this.m_computerPlayer.getOwnGrid().displayGrid();
    }



    public Player getOpponent(Player p) {
        return (p == this.m_humanPlayer) ? this.m_computerPlayer : this.m_humanPlayer;
    }



    private void processScan(Player attacker, Player defender, List<Coordinate> targets) {

    }


}