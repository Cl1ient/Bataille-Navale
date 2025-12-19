package model;

import model.game.Game;
import model.map.Grid;
import model.player.Player;

public interface GridEntity {
    public void onHit(Player attacker, Player defender, Integer x, Integer y, Integer index);
    public EntityType getType();
    public Integer getSize();
    boolean isSunk();
    void registerToGrid(Grid grid);


}
