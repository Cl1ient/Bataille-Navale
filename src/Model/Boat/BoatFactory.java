package Model.Boat;

/**
 * factory of boat
 */
public interface BoatFactory {
    public Boat createBoat(BoatType boatType);
}
