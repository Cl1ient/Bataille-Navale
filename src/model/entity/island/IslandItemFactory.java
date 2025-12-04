package model.entity.island;

import model.GridEntity;

public class IslandItemFactory {
    /**
     * Constructor of Island Factory
     */
    public IslandItemFactory(){}

    public NewStorm createNewItemStorm(){
        return new NewStorm();
    }

    public GridEntity createNewItemBomb(){
        return new NewBomb();
    }

    public GridEntity createNewItemSonar(){
        return new NewSonar();
    }

    public GridEntity createNEwItemBlackHole(){
        return new NewBlackHole();
    }
}
