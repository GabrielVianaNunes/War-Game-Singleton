package war_singleton;

class Player {
    private String name;
    private java.awt.Color color;

    public Player(String name, java.awt.Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public java.awt.Color getColor() {
        return color;
    }
}