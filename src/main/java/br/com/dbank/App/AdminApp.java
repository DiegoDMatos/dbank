package br.com.dbank.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin.fxml"));
        Parent root = loader.load();

        // Defina o tamanho explicitamente aqui para teste
        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("DBank - Painel Administrativo");
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}