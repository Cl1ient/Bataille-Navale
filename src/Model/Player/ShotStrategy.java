package Model.Player;

import Model.Coordinates;
import Model.Map.Grid;
import Model.ShotResult;

public interface ShotStrategy {
    Coordinates getNextShot(Grid shotsGrid, ShotResult lastResult);
}
