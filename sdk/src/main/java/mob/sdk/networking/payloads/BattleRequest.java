package mob.sdk.networking.payloads;

import java.io.Serializable;

public class BattleRequest implements Serializable {
    private final String tableId;
    private final Color teamColor;

    public BattleRequest(String tableId, Color teamColor) {
        this.tableId = tableId;
        this.teamColor = teamColor;
    }

    public String getTableId() {
        return tableId;
    }

    public Color getTeamColor() {
        return teamColor;
    }

    public enum Color {
        RED,
        BLUE
    }
}
