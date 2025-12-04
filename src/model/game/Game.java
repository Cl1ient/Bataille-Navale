package model.game;

import model.Coordinate;
import model.weapon.Weapon;
import model.player.HumanPlayer;
import model.player.ComputerPlayer;
import model.player.Player;

import java.util.List;

public class Game {
    private Player m_humanPlayer;
    private Player m_computerPlayer;
    private Player m_currentPlayer;

    public Game(GameConfiguration config){

        this.m_humanPlayer = new HumanPlayer(config);
        this.m_computerPlayer = new ComputerPlayer(config);
        this.m_currentPlayer = m_humanPlayer;

        this.displayGridPlayer();
    }

    public void displayGridPlayer(){
        System.out.println("grille de : " + this.m_humanPlayer.getNickName());
        this.m_humanPlayer.getOwnGrid().displayGrid();

        System.out.println("grille de : " + this.m_computerPlayer.getNickName());
        this.m_computerPlayer.getOwnGrid().displayGrid();
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

    public Player getOpponent(Player p) {
        return (p == this.m_humanPlayer) ? this.m_computerPlayer : this.m_humanPlayer;
    }

    public void processOffensiveAttack(Player attacker, Player defender, List<Coordinate> coord){}

    private void processScan(Player attacker, Player defender, List<Coordinate> targets) {

    }


}