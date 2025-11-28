package Model.entity.island;

public class IslandItemFactory {
    /**
     * Constructor of Island Factory
     */
    public IslandItemFactory(){}

    public NewStorm createNewItemStorm(){
        return new NewStorm();
    }

    public NewBomb createNewItemBomb(){
        return new NewBomb();
    }

    public NewSonar createNewItemSonar(){
        return new NewSonar();
    }

    public NewBlackHole createNEwItemBlackHole(){
        return new NewBlackHole();
    }
}
