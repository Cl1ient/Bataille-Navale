package Model.Boat;

import Model.Boat.Type.*;

public class BoatFactory {
    public BoatFactory(){}

    public AirCraftCarrier createAirCraftCarrier(){return new AirCraftCarrier();}
    public Cruiser createCruiser(){return new Cruiser();}
    public Destroyer createDestroyer(){return new Destroyer();}
    public Submarine createSubmarine(){return new Submarine();}
    public Torpedo createTorpedo(){return new Torpedo();}
}
