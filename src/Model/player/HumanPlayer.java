package Model.player;

import Model.Coordinate;
import Model.EntityType;
import Model.Game.GameConfiguration;

import java.util.List;
import java.util.Map;

public class HumanPlayer extends Player{
    public HumanPlayer(GameConfiguration config){
        super(config);
    }
    @Override
    public void placeEntity(Map<EntityType, List<Coordinate>> entityPlacement) {
        this.m_ownGrid.placeEntity(entityPlacement);
    }
}