package br.com.rhmanager;

import br.com.rhmanager.view.TelaPrincipal;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        TelaPrincipal telaPrincipal = new TelaPrincipal();
        Scene scene = new Scene(telaPrincipal.montarTela(), 900, 600);
        stage.setTitle("Sistema de Gestão de Recursos Humanos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}