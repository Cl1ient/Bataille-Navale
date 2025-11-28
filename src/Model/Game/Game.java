package Model.Game;

import Model.player.HumanPlayer;
import Model.player.ComputerPlayer;
import Model.Coordinate;
import Model.Weapon.Weapon;
import Model.player.Player;

public class Game {
    private Player m_humanPlayer;
    private Player m_computerPlayer;
    private Player m_currentPlayer;

    public Game(GameConfiguration config){
        this.m_humanPlayer = new HumanPlayer(config);
        this.m_computerPlayer = new ComputerPlayer(config);
        this.m_currentPlayer = m_humanPlayer;
    }


}