package model.player;

import model.game.GameConfiguration;

public class HumanPlayer extends Player{
    public HumanPlayer(GameConfiguration config){
        super(config);
        this.placeEntity(config.getGridEntityPlacement());
    }

}