package Model.Game;

import Model.Boat.BoatType;

import java.util.Map;

public class GameConfiguration {
    private final Integer m_gridSize;
    private final Map<BoatType, Integer> m_boatCounts;
    private final boolean m_isIslandMode;
    private final Integer m_numSpecialItems;
    private final Integer m_computerShotLevel;
    private final Integer m_placementLevel;

    /**
     * Constructor for GameConfiguration.
     * @param gridSize The size of the grid (6 to 10).
     * @param boatCounts A map defining the number of boats for each BoatType .
     * @param isIslandMode True if the Island mode is enabled.
     * @param numSpecialItems The complexity level for items .
     * @param computerShotLevel The complexity level for shooting .
     * @param placementLevel The complexity level for human placement .
     */
    public GameConfiguration(
            Integer gridSize,
            Map<BoatType, Integer> boatCounts,
            boolean isIslandMode,
            Integer numSpecialItems,
            Integer computerShotLevel,
            Integer placementLevel
    )
    {
        this.m_gridSize = gridSize;
        this.m_boatCounts = boatCounts;
        this.m_isIslandMode = isIslandMode;
        this.m_numSpecialItems = numSpecialItems;
        this.m_computerShotLevel = computerShotLevel;
        this.m_placementLevel = placementLevel;
    }

    public Integer getGridSize() {
        return m_gridSize;
    }

    public Map<BoatType, Integer> getBoatCounts() {
        return m_boatCounts;
    }

    public boolean isIslandMode() {
        return m_isIslandMode;
    }

    public Integer getNumSpecialItems() {
        return m_numSpecialItems;
    }

    public Integer getComputerShotLevel() {
        return m_computerShotLevel;
    }

    public Integer getPlacementLevel() {
        return m_placementLevel;
    }
}
