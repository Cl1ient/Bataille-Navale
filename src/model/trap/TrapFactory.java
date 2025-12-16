package model.trap;

import model.GridEntity;

public class TrapFactory {
    public TrapFactory(){}

    public GridEntity createStorm(boolean islandMod){return new Storm(islandMod);}
    public GridEntity createBlackHole(boolean islandMod){return new BlackHole(islandMod);}
}
