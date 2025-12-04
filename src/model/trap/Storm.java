package model.trap;

import model.Coordinate;
import model.EntityType;
import model.game.Game;
import model.GridEntity;
import model.player.Player;

import java.util.Random;

public class Storm implements GridEntity {
    private Integer m_size = 1;
    private Integer turnsLeft = 3;
    private final EntityType m_type = EntityType.STORM;

    public void onHit(Game game, Player attacker, Player defender, Integer x, Integer y){
        this.turnsLeft --;
        Coordinate coord = modifyCoordinates(defender.getGridSize());
        //defender.getOwnGrid().markHitTrap(coord);
        //game.proccessShot(attacker, defender, coord.getX(), coord.getY());
    }

    public Coordinate modifyCoordinates(Integer size){
        // renvoie une coordonée aléatoire
        Random rand = new Random();

        int x = rand.nextInt(size);
        int y = rand.nextInt(size);

        return new Coordinate(x,y);
    }

    @Override
    public EntityType getType(){return this.m_type;}

    public Integer getSize() {return this.m_size;}

}
