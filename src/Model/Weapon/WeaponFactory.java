package Model.Weapon;

public class WeaponFactory {

    public WeaponFactory(){}

    public Sonar createSonar(){
        return new Sonar();
    }

    public Bombe createBomb(){
        return new Bombe();
    }

    public Missile createMissile(){
        return new Missile();
    }
}
