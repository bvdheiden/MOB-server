package mob.sdk.networking;

public interface LoggingCallback {
    void print(String string);
    void printf(String string,Object... params);
}
