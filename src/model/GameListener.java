package model;

import model.player.Player;

import java.util.List;
import java.util.Map;

public interface GameListener {
    void onScanResult(Player player, List<ScanResult> results);
    void onGameOver(Player winner);
    void turnEnded(Player a, Map<Coordinate, String> s);
    void onCellUpdated(Player defender, Coordinate coord);
    void onShipSunk(Player defender);
}
