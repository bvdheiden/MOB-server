package mob.server.networking;

import mob.sdk.cards.CardRepository;
import mob.sdk.networking.LoggingCallback;
import mob.sdk.networking.SocketClient;
import mob.sdk.networking.Transaction;
import mob.sdk.networking.TransactionType;
import mob.sdk.networking.payloads.*;
import mob.server.networking.mqtt.BattleDevice;
import mob.server.networking.mqtt.CardDevice;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class MobServer implements LoggingCallback {
    private final AtomicBoolean connecting = new AtomicBoolean(false);
    private final AtomicBoolean mqttConnected = new AtomicBoolean(false);
    private final List<SocketClient> socketClientList = new CopyOnWriteArrayList<>();

    private final Map<String, BattleDevice> battleDeviceMap = new ConcurrentHashMap<>();
    private final Map<String, CardDevice> cardDeviceMap = new ConcurrentHashMap<>();

    private ServerSocket serverSocket;

    public MobServer(MqttClient mqttClient) {
        mqttClient.addSubscription(MqttClient.TOPIC_PREFIX + "connect", bytes -> {
            String[] payload = new String(bytes).split(":");

            String deviceType = payload[0];
            String deviceId = payload[1];

            switch (deviceType) {
                case "battle":
                    BattleDevice battleDevice = new BattleDevice(deviceId, mqttClient);

                    battleDeviceMap.put(deviceId, battleDevice);
                    break;
                case "card":
                    CardDevice cardDevice = new CardDevice(deviceId, mqttClient);

                    appointNewCard(cardDevice);

                    cardDeviceMap.put(deviceId, cardDevice);
                    break;
            }
        });

        mqttClient.addConnectionListener(() -> {
            mqttConnected.set(true);
        });

        mqttClient.addDisconnectionListener(() -> {
            mqttConnected.set(false);
        });
    }

    /**
     * Start the server socket and start listening for clients.
     */
    public void start(int port) {
        SocketClient.addLoggingCallback(this);

        if (isRunning()) {
            return;
        }

        connecting.set(true);

        new Thread(() -> {
            try {
                SocketClient.print("Creating server socket.");
                this.serverSocket = new ServerSocket(port);
                while (connecting.get()) {
                    try {
                        SocketClient.print("Waiting for clients.");
                        Socket socket = serverSocket.accept();
                        SocketClient.print("Client accepted.");
                        SocketClient client = new SocketClient(socket);
                        socketClientList.add(client);

                        client.addTransactionListener((transaction) -> {
                            switch (transaction.getType()) {
                                case CARD_REQUEST -> onCardRequest(client, (CardRequest) transaction.getPayload());
                                case BATTLE_REQUEST -> onBattleRequest(client, (BattleRequest) transaction.getPayload());
                            }
                        });

                        client.addDisconnectionListener(() -> {

                        });
                    } catch (IOException exception) {
                        SocketClient.print("Failed to accept and create client.");
                    }
                }
            } catch (IOException exception) {
                SocketClient.print("Failed to create server socket.");
            }
        }).start();
    }

    /**
     * Stop the server socket and stop listening for clients.
     */
    public void stop() {
        if (!isRunning()) {
            return;
        }

        connecting.set(false);

        for (SocketClient serverClient : socketClientList) {
            serverClient.stop();
        }

        try {
            serverSocket.close();
        } catch (IOException exception) {
            SocketClient.print("Failed to close server socket.");
        }
    }

    public boolean isRunning() {
        return connecting.get() || (serverSocket != null && !serverSocket.isClosed());
    }

    /**
     * Appoints a new card and associated unique card code to a card device.
     *
     * @param cardDevice card device
     */
    public synchronized void appointNewCard(CardDevice cardDevice) {
        List<String> usedCardIdList = new ArrayList<>();
        List<String> usedCardCodeList = new ArrayList<>();

        // collect used card ids and codes
        for (CardDevice usedCardDevice : cardDeviceMap.values()) {
            if (cardDevice == usedCardDevice)
                continue;

            usedCardIdList.add(usedCardDevice.getCardCode());
            usedCardCodeList.add(usedCardDevice.getCardCode());
        }

        // filter for available card ids
        List<String> availableCardIdList = CardRepository.INSTANCE.getCardIds().stream()
                .filter(cardId -> !usedCardIdList.contains(cardId))
                .collect(Collectors.toList());

        // get random available card id
        String cardId = availableCardIdList.get(new Random().nextInt(availableCardIdList.size() - 1));

        // generate new unique card code
        String cardCode;
        do {
            cardCode = generateCardCode();
        } while (!usedCardCodeList.contains(cardCode));

        // assign new card
        cardDevice.setCard(cardId, cardCode);
    }


    private String generateCardCode() {
        return ""; // @todo make card code generator
    }

    /**
     * Handle client card request.
     *
     * @param client      client
     * @param cardRequest request
     */
    private void onCardRequest(SocketClient client, CardRequest cardRequest) {
        for (CardDevice cardDevice : cardDeviceMap.values()) {
            // check if card code matches
            if (!cardDevice.getCardCode().equals(cardRequest.getCardCode()))
                continue;

            // check if card is claimed for client
            if (cardDevice.isClaimed(client))
                break;

            cardDevice.setClaimed(client);

            // send card result
            client.send(new Transaction(TransactionType.CARD_RESULT, new CardResult(cardDevice.getCardId())));
            cardDevice.publish("claimed");
            return;
        }

        // send card invalid
        client.send(new Transaction(TransactionType.CARD_REQUEST_INVALID, new CardRequestInvalid(cardRequest.getCardCode())));
    }

    /**
     * Handle client battle request.
     *
     * @param client        client
     * @param battleRequest request
     */
    private void onBattleRequest(SocketClient client, BattleRequest battleRequest) {
        // check if table with id exists
        if (!battleDeviceMap.containsKey(battleRequest.getTableId())) {
            client.send(new Transaction(TransactionType.BATTLE_REQUEST_INVALID, new BattleRequestInvalid(BattleRequestInvalid.REASON.DEVICE_ID_WRONG)));
            return;
        }

        BattleDevice battleDevice = battleDeviceMap.get(battleRequest.getTableId());

        // check if table is already playing
        if (battleDevice.isPlaying().get()) {
            client.send(new Transaction(TransactionType.BATTLE_REQUEST_INVALID, new BattleRequestInvalid(BattleRequestInvalid.REASON.ALREADY_PLAYING)));
            return;
        }

        // check if color is already selected
        if (!battleDevice.setClient(battleRequest.getTeamColor(), client)) {
            client.send(new Transaction(TransactionType.BATTLE_REQUEST_INVALID, new BattleRequestInvalid(BattleRequestInvalid.REASON.TEAM_ALREADY_TAKEN)));
            return;
        }

        if (battleDevice.isReady()) {
            // start game when both teams are ready
            battleDevice.setOnAbort(battleDevice::reset);

            battleDevice.setOnFinish((redWins, redClient, blueWins, blueClient) -> {
                redClient.send(new Transaction(TransactionType.BATTLE_RESULT, new BattleResult(redWins, blueWins)));
                blueClient.send(new Transaction(TransactionType.BATTLE_RESULT, new BattleResult(redWins, blueWins)));
                battleDevice.reset();
            });

            battleDevice.sendReady();
        }
    }

    @Override
    public void print(String string) {
        System.out.println(string);
    }

    @Override
    public void printf(String string, Object... params) {
        System.out.printf(string + "%n", params);
    }
}
