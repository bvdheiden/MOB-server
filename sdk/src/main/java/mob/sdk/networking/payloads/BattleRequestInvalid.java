package mob.sdk.networking.payloads;

public class BattleRequestInvalid {
    private final Field field;

    public BattleRequestInvalid(Field field) {
        this.field = field;
    }

    public Field getField() {
        return field;
    }

    public enum Field {
        TABLE_ID,
        TABLE_COLOR
    }
}
