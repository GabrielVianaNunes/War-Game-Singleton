package war_singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

class GameBoard {
    private static GameBoard uniqueInstance;
    private List<String> territories;
    private List<Boolean> territoryOwnership;
    private List<String> territoryOwners;

    private GameBoard() {
        territories = new ArrayList<>();
        territoryOwnership = new ArrayList<>();
        territoryOwners = new ArrayList<>();
    }

    public static GameBoard getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new GameBoard();
        }
        return uniqueInstance;
    }

    public void initializeBoard(int numTerritories) {
        territories.clear();
        territoryOwnership.clear();
        territoryOwners.clear();

        for (int i = 1; i <= numTerritories; i++) {
            territories.add("Território " + i);
            territoryOwnership.add(false);
            territoryOwners.add("");
        }
    }

    public boolean isTerritoryOccupied(int index) {
        return territoryOwnership.get(index);
    }

    public void occupyTerritory(int index, String playerName) {
        territoryOwnership.set(index, true);
        territoryOwners.set(index, playerName);
    }

    public String getTerritoryOwner(int index) {
        return territoryOwners.get(index);
    }

    public List<String> getTerritories() {
        return territories;
    }
}
