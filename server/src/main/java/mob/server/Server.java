package mob.server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import mob.server.networking.MOBServer;
import mob.server.networking.MQTTClient;

import java.io.*;
import java.util.StringJoiner;

public class Server extends Application {
    private MQTTClient mqttClient = new MQTTClient();
    private MOBServer server = new MOBServer(mqttClient);
    private String lastBroker;
    private String lastUsername;
    private String lastPassword;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Label serverLabel = new Label("Server");
        Button serverStartButton = new Button("Start");
        serverStartButton.setOnAction(actionEvent -> {
            if (server.isRunning()) {
                server.stop();
                serverStartButton.setText("Start");
            } else {
                server.start(10_000);
                serverStartButton.setText("Stop");
            }
        });

        VBox serverLayout = new VBox(serverLabel, serverStartButton);

        Label mqttBrokerLabel = new Label("Broker: ");
        TextField mqttBrokerField = new TextField();
        Label mqttUsernameLabel = new Label("Username: ");
        TextField mqttUsernameField = new TextField();
        Label mqttPasswordLabel = new Label("Password: ");
        TextField mqttPasswordField = new PasswordField();

        HBox mqttForm = new HBox(mqttBrokerLabel, mqttBrokerField, mqttUsernameLabel, mqttUsernameField, mqttPasswordLabel, mqttPasswordField);

        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(new File("login.dat")))) {
            String[] login = dataInputStream.readUTF().split("`-`");
            mqttBrokerField.setText(login[0]);
            mqttUsernameField.setText(login[1]);
            mqttPasswordField.setText(login[2]);
        } catch (Exception e) { }

        Label mqttClientLabel = new Label("MQTT client");
        Button mqttClientStartButton = new Button("Start");
        mqttClientStartButton.setOnAction(actionEvent -> {
            if (mqttClient.isRunning()) {
                mqttClient.stop();
            } else {
                this.lastBroker = mqttBrokerField.getText().trim();
                this.lastUsername = mqttUsernameField.getText().trim();
                this.lastPassword = mqttPasswordField.getText().trim();

                if (lastBroker.length() > 0 && lastUsername.length() > 0 && lastPassword.length() > 0) {
                    mqttClient.start(lastBroker, lastUsername, lastPassword);
                }
            }
        });

        mqttClient.addConnectionListener(() -> {
            Platform.runLater(() -> {
                mqttClientStartButton.setText("Stop");

                try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(new File("login.dat")))) {
                    dataOutputStream.writeUTF(new StringJoiner("`-`").add(lastBroker).add(lastUsername).add(lastPassword).toString());
                } catch (IOException e) { }
            });
        });

        mqttClient.addDisconnectionListener(() -> {
            Platform.runLater(() -> {
                mqttClientStartButton.setText("Start");
            });
        });

        VBox mqttLayout = new VBox(mqttClientLabel, mqttForm, mqttClientStartButton);

        primaryStage.setScene(new Scene(new VBox(serverLayout, mqttLayout)));
        primaryStage.setTitle("Server");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        server.stop();
        mqttClient.stop();
    }
}
