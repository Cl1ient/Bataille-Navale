package view.placementView;

import controller.GameController;
import model.Coordinate;
import model.EntityType;

import java.util.*;
import java.util.stream.Collectors;

public class PlacementModel {

    private final int m_gridSize;
    private final boolean m_islandMode;
    private final Map<EntityType, Integer> m_boatsToPlace;
    private final Map<EntityType, List<Coordinate>> m_placedEntities;

    private EntityType m_selectedEntityType;
    private boolean m_isHorizontal = true;

    public PlacementModel(GameController controller, Map<EntityType, Integer> m_boatsToPlace) {
        this.m_gridSize = controller.getGameConfiguration().getGridSize();
        this.m_islandMode = controller.getGameConfiguration().isIslandMode();
        this.m_boatsToPlace = new HashMap<>(m_boatsToPlace);
        this.m_placedEntities = new HashMap<>();

        updateSelectionAuto();
    }

    public boolean validatePlacement(List<Coordinate> coords) {
        for (Coordinate coord : coords) {
            if (coord.getX() < 0 || coord.getX() >= m_gridSize || coord.getY() < 0 || coord.getY() >= m_gridSize) {
                return false;
            }
            if (m_islandMode && coord.getX() >= 3 && coord.getX() <= 6 && coord.getY() >= 3 && coord.getY() <= 6) {
                return false;
            }
            if (isOccupied(coord)) {
                return false;
            }
        }
        return true;
    }

    public void placeEntity(List<Coordinate> coords) {
        if (m_selectedEntityType != null && validatePlacement(coords)) {
            m_placedEntities.computeIfAbsent(m_selectedEntityType, k -> new ArrayList<>()).addAll(coords);
            decrementBoatCount(m_selectedEntityType);
            updateSelectionAuto();
        }
    }

    private boolean isOccupied(Coordinate coord) {
        return m_placedEntities.values().stream()
                .flatMap(List::stream)
                .anyMatch(c -> c.getX() == coord.getX() && c.getY() == coord.getY());
    }

    public List<Coordinate> calculateCoordinates(int r, int c) {
        List<Coordinate> coords = new ArrayList<>();
        if (m_selectedEntityType == null) return coords;

        int size = getEntitySize(m_selectedEntityType);
        for (int i = 0; i < size; i++) {
            int row = m_isHorizontal ? r : r + i;
            int col = m_isHorizontal ? c + i : c;
            coords.add(new Coordinate(row, col));
        }
        return coords;
    }

    private int getEntitySize(EntityType type) {
        return switch (type) {
            case AIRCRAFT_CARRIER -> 5;
            case CRUISER -> 4;
            case DESTROYER, SUBMARINE -> 3;
            case TORPEDO -> 2;
            default -> 1;
        };
    }

    public void updateSelectionAuto() {
        List<EntityType> available = getAvailableTypes();
        if (!available.isEmpty() && (m_selectedEntityType == null || m_boatsToPlace.get(m_selectedEntityType) <= 0)) {
            m_selectedEntityType = available.get(0);
        } else if (available.isEmpty()) {
            m_selectedEntityType = null;
        }
    }

    public List<EntityType> getAvailableTypes() {
        boolean boatsRemaining = m_boatsToPlace.entrySet().stream()
                .anyMatch(entry -> entry.getValue() > 0 && isBoat(entry.getKey()));

        return m_boatsToPlace.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .map(Map.Entry::getKey)
                .filter(type -> boatsRemaining == isBoat(type))
                .collect(Collectors.toList());
    }

    private void decrementBoatCount(EntityType type) {
        m_boatsToPlace.put(type, m_boatsToPlace.get(type) - 1);
    }

    public boolean isFinished() {
        return m_boatsToPlace.values().stream().mapToInt(Integer::intValue).sum() == 0;
    }

    public int getRemainingCount() {
        return m_boatsToPlace.values().stream().mapToInt(Integer::intValue).sum();
    }

    public EntityType getPlacedEntityTypeAt(int r, int c) {
        for (Map.Entry<EntityType, List<Coordinate>> entry : m_placedEntities.entrySet()) {
            for (Coordinate coord : entry.getValue()) {
                if (coord.getX() == r && coord.getY() == c) return entry.getKey();
            }
        }
        return null;
    }

    public static boolean isBoat(EntityType type) {
        if (type == null) return false;
        return switch (type) {
            case AIRCRAFT_CARRIER, CRUISER, DESTROYER, SUBMARINE, TORPEDO -> true;
            default -> false;
        };
    }

    public void placeAllRandomly() {
        Random rand = new Random();
        List<EntityType> remainingTypes = getAvailableTypes();
        while (!remainingTypes.isEmpty()) {
            EntityType type = remainingTypes.get(0);
            boolean placed = false;
            int attempts = 0;
            while (!placed && attempts < 200) {
                int r = rand.nextInt(m_gridSize);
                int c = rand.nextInt(m_gridSize);
                boolean horizontal = rand.nextBoolean();
                boolean previousOrientation = this.m_isHorizontal;
                this.m_isHorizontal = horizontal;
                this.m_selectedEntityType = type;
                List<Coordinate> coords = calculateCoordinates(r, c);
                if (validatePlacement(coords)) {
                    placeEntity(coords);
                    placed = true;
                }
                this.m_isHorizontal = previousOrientation;
                attempts++;
            }
            if (!placed) {
                System.out.println("Impossible de placer automatiquement : " + type);
                break;
            }
            remainingTypes = getAvailableTypes();
        }
        updateSelectionAuto();
    }

    public void setSelectedEntityType(EntityType type) { this.m_selectedEntityType = type; }
    public EntityType getSelectedEntityType() { return m_selectedEntityType; }
    public void setHorizontal(boolean horizontal) { m_isHorizontal = horizontal; }
    public Map<EntityType, List<Coordinate>> getPlacedEntities() { return m_placedEntities; }
    public int getGridSize() { return m_gridSize; }
    public boolean isIslandMode() { return m_islandMode; }
}