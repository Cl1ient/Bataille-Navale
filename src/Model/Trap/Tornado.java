package Model.Trap;

import Model.Coordinate;
import Model.Game.Game;
import Model.GridEntity;
import Model.player.Player;

import java.util.Random;

public class Tornado implements GridEntity {
    private Integer size = 3;
    private Integer turnsLeft = 3;

    public void onHit(Game game, Player attacker, Player defender, Integer x, Integer y){
        this.turnsLeft --;
        Coordinate coord = modifyCoordinates(defender.getGridSize());
        defender.getOwnGrid().markHitTrap(coord);
        game.proccessShot(attacker, defender, coord.getX(), coord.getY());
    }

    public Coordinate modifyCoordinates(Integer size){
        // renvoie une coordonée aléatoire
        Random rand = new Random();

        int x = rand.nextInt(size);
        int y = rand.nextInt(size);

        return new Coordinate(x,y);
    }

    @Override
    public String getType() {
        return "Tornado";
    }

}
