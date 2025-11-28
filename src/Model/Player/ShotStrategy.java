package Model.Player;

import Model.Coordinate;
import Model.Map.Grid;
import Model.ShotResult;

public interface ShotStrategy {
    Coordinate getNextShot(Grid shotsGrid, ShotResult lastResult);
}
