package Model.Game;

import Model.player.HumanPlayer;
import Model.player.ComputerPlayer;
import Model.player.Player;

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


}