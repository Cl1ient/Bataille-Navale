package model;

import model.trap.Trap;

public interface IslandListener {
    public void notifyPlaceIslandEntity(Trap entity);
    public void notifyWeaponFind(EntityType weaponType);
}
