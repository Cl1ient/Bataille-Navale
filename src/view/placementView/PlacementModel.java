package view.placementView;

import controller.GameController;
import model.Coordinate;
import model.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Gère les données et la logique de placement (Validation, Stockage).
 */
public class PlacementModel {

    private final int gridSize;
    private final boolean islandMode;
    private final Map<EntityType, Integer> boatsToPlace;
    private final Map<EntityType, List<Coordinate>> placedEntities;

    private EntityType selectedEntityType;
    private boolean isHorizontal = true;

    public PlacementModel(GameController controller, Map<EntityType, Integer> boatsToPlace) {
        this.gridSize = controller.getGameConfiguration().getGridSize();
        this.islandMode = controller.getGameConfiguration().isIslandMode();
        this.boatsToPlace = new HashMap<>(boatsToPlace);
        this.placedEntities = new HashMap<>();

        updateSelectionAuto();
    }

    public boolean validatePlacement(List<Coordinate> coords) {
        for (Coordinate coord : coords) {
            if (coord.getX() < 0 || coord.getX() >= gridSize || coord.getY() < 0 || coord.getY() >= gridSize) {
                return false;
            }
            if (islandMode && coord.getX() >= 3 && coord.getX() <= 6 && coord.getY() >= 3 && coord.getY() <= 6) {
                return false;
            }
            if (isOccupied(coord)) {
                return false;
            }
        }
        return true;
    }

    public void placeEntity(List<Coordinate> coords) {
        if (selectedEntityType != null && validatePlacement(coords)) {
            placedEntities.computeIfAbsent(selectedEntityType, k -> new ArrayList<>()).addAll(coords);
            decrementBoatCount(selectedEntityType);
            updateSelectionAuto();
        }
    }

    private boolean isOccupied(Coordinate coord) {
        return placedEntities.values().stream()
                .flatMap(List::stream)
                .anyMatch(c -> c.getX() == coord.getX() && c.getY() == coord.getY());
    }

    public List<Coordinate> calculateCoordinates(int r, int c) {
        List<Coordinate> coords = new ArrayList<>();
        if (selectedEntityType == null) return coords;

        int size = getEntitySize(selectedEntityType);
        for (int i = 0; i < size; i++) {
            int row = isHorizontal ? r : r + i;
            int col = isHorizontal ? c + i : c;
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
        if (!available.isEmpty() && (selectedEntityType == null || boatsToPlace.get(selectedEntityType) <= 0)) {
            selectedEntityType = available.get(0);
        } else if (available.isEmpty()) {
            selectedEntityType = null;
        }
    }

    public List<EntityType> getAvailableTypes() {
        boolean boatsRemaining = boatsToPlace.entrySet().stream()
                .anyMatch(entry -> entry.getValue() > 0 && isBoat(entry.getKey()));

        return boatsToPlace.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .map(Map.Entry::getKey)
                .filter(type -> boatsRemaining == isBoat(type))
                .collect(Collectors.toList());
    }

    private void decrementBoatCount(EntityType type) {
        boatsToPlace.put(type, boatsToPlace.get(type) - 1);
    }

    public boolean isFinished() {
        return boatsToPlace.values().stream().mapToInt(Integer::intValue).sum() == 0;
    }

    public int getRemainingCount() {
        return boatsToPlace.values().stream().mapToInt(Integer::intValue).sum();
    }

    public EntityType getPlacedEntityTypeAt(int r, int c) {
        for (Map.Entry<EntityType, List<Coordinate>> entry : placedEntities.entrySet()) {
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

    public void setSelectedEntityType(EntityType type) { this.selectedEntityType = type; }
    public EntityType getSelectedEntityType() { return selectedEntityType; }
    public void setHorizontal(boolean horizontal) { isHorizontal = horizontal; }
    public Map<EntityType, List<Coordinate>> getPlacedEntities() { return placedEntities; }
    public int getGridSize() { return gridSize; }
    public boolean isIslandMode() { return islandMode; }
}