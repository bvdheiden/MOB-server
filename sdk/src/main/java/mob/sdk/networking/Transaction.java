package mob.sdk.networking;

import java.io.Serializable;

public class Transaction implements Serializable {
    private TransactionType type;
    private Serializable payload;

    public Transaction(TransactionType type) {
        this.type = type;
    }

    public Transaction(TransactionType type, Serializable payload) {
        this.type = type;
        this.payload = payload;
    }

    public Object getPayload() {
        return payload;
    }

    public TransactionType getType() {
        return type;
    }

    public boolean isType(TransactionType type) {
        return this.type == type;
    }
}
