package war_singleton;

import java.awt.Color;

public class Player {
    private String name;
    private Color color;
    private int armies;

    public Player(String name, Color color, int armies) {
        this.name = name;
        this.color = color;
        this.armies = armies;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public int getArmies() {
        return armies;
    }

    public void setArmies(int armies) {
        this.armies = armies;
    }
}
