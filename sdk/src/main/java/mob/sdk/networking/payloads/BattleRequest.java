package mob.sdk.networking.payloads;

import java.io.Serializable;

public class BattleRequest implements Serializable {
    private final int tableNumber;
    private final Color color;

    public BattleRequest(int tableNumber, Color color) {
        this.tableNumber = tableNumber;
        this.color = color;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public Color getColor() {
        return color;
    }

    public enum Color {
        RED,
        BLUE
    }
}
