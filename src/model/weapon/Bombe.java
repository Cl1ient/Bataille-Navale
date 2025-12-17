package model.weapon;

import model.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class Bombe implements Weapon{
    private Integer m_useLeft = 1;


    @Override
    public List<Coordinate> generateTargets(Coordinate coord, int gridSize){
       List<Coordinate> targets = new ArrayList<>();

        List<Coordinate> potentialTargets = new ArrayList<>();
        potentialTargets.add(coord);
        potentialTargets.add(coord.getRelative(0, 1));  // Droite
        potentialTargets.add(coord.getRelative(0, -1)); // Gauche
        potentialTargets.add(coord.getRelative(1, 0));  // HAUT
        potentialTargets.add(coord.getRelative(-1, 0)); // Bas

        for (Coordinate potential : potentialTargets) {
            int x = potential.getX();
            int y = potential.getY();
            if (x >= 0 && x < gridSize && y >= 0 && y < gridSize) {
                targets.add(potential);
            }
        }
        return targets;
    }

    @Override
    public String getName(){
        return "BOMB";
    }

    @Override
    public Integer getUsesLeft(){
        return m_useLeft;
    }

    @Override
    public void setUsesLeft(){
        m_useLeft++;
    }
    public boolean isOffensive(){return true;}

    @Override
    public void use(){
        if(this.m_useLeft > 0 ){
            this.m_useLeft--;
        }
    }

}
