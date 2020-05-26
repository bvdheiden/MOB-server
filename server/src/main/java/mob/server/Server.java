package mob.server;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import mob.server.networking.MOBServer;

public class Server extends Application {
    private MOBServer server = new MOBServer();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Button startButton = new Button("Start");
        startButton.setOnAction(actionEvent -> {
            if (server.isRunning()) {
                server.stop();
                startButton.setText("Start");
            } else {
                server.start(10_000);
                startButton.setText("Stop");
            }
        });

        primaryStage.setScene(new Scene(startButton));
        primaryStage.setTitle("Server");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        server.stop();
    }
}
