package Model;

import Model.Weapon.Weapon;

public class ShotResult {
    private boolean m_isHit;
    private boolean m_isSunk;
    private String m_EntityType;
    private Coordinates m_coordinates;
    private  String m_message;
    private boolean m_isTrap;
    private Weapon m_acquiredWeapon;
    private boolean m_isVisible;

    /**
     * Constructs the result of a shot
     * @param isHit True if the shoat is an entity
     * @param isSunk True if a boat was sunk
     * @param EntityType Type of the entity hit
     * @param coordinates Coord where the shot landed
     * @param message Descriptive message
     * @param isTrap True if the hit entity was a Trap
     * @param acquiredWeapon Weapon object found in island
     * @param isVisible If entity is visible in the grid
     */
    public ShotResult(
            boolean isHit,
            boolean isSunk,
            String EntityType,
            Coordinates coordinates,
            String message,
            boolean isTrap,
            Weapon acquiredWeapon,
            boolean isVisible
    ) {
        this. m_isHit = isHit;
        this.m_isSunk = isSunk;
        this.m_EntityType = EntityType;
        this.m_coordinates = coordinates;
        this.m_message = message;
        this.m_isTrap = isTrap;
        this.m_acquiredWeapon = acquiredWeapon;
        this.m_isVisible = isVisible;
    }

    public boolean isHit() {
        return m_isHit;
    }

    public boolean isSunk() {
        return m_isSunk;
    }

    public String getEntityType() {
        return m_EntityType;
    }

    public Coordinates getCoordinates() {
        return m_coordinates;
    }

    public String getMessage() {
        return m_message;
    }

    public boolean isTrap() {
        return m_isTrap;
    }

    public Weapon getAcquiredWeapon(){
        return m_acquiredWeapon;
    }

    public boolean isVisible() {
        return m_isVisible;
    }
}
