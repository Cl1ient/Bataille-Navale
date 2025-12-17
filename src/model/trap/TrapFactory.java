package model.trap;

import model.GridEntity;

public class TrapFactory {
    public TrapFactory(){}

    public GridEntity createStorm(boolean p){return new Storm(p);}
    public GridEntity createBlackHole(boolean p){return new BlackHole(p);}
}
