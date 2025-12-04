package model;

import model.player.Player;

import java.util.List;

public interface GameListener {
    void onScanResult(Player player, List<ScanResult> results);
    void onGameOver(Player winner);
    void onCellUpdated(Player defender, Coordinate coord);

    void onShipSunk(Player defender);
}
