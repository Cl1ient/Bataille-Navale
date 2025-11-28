package Model.Game;

import Model.Boat.BoatType;
import Model.Coordinate;
import Model.EntityType;

import java.util.List;
import java.util.Map;

public class GameConfiguration {
    private String m_nickName;
    private final Integer m_gridSize;
    private Map<EntityType, List<Coordinate>> m_gridEntityPlacement;
    private final boolean m_isIslandMode;

    public GameConfiguration(
            Integer gridSize,
            Map<EntityType, List<Coordinate>> boatsPlacement,
            boolean isIslandMode,
            String name
    )
    {
        this.m_gridSize = gridSize;
        this.m_gridEntityPlacement = boatsPlacement;
        this.m_isIslandMode = isIslandMode;
        this.m_nickName = name;
    }

    public Integer getGridSize() {
        return m_gridSize;
    }
    public String getNickName() {
        return m_nickName;
    }

    public Map<EntityType, List<Coordinate>> getGridEntityPlacement() {
        return m_gridEntityPlacement;
    }

    public boolean isIslandMode() {
        return m_isIslandMode;
    }
}
