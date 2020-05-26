package mob.sdk.networking.listeners;

import mob.sdk.networking.Transaction;

@FunctionalInterface
public interface TransactionListener {
    void onTransaction(Transaction transaction);
}
