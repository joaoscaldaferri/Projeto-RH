package rhpoo;

import rhpoo.visual.TelaRH;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        TelaRH telaRH = new TelaRH();
        Scene scene = new Scene(telaRH.montarTela(), 900, 600);
        stage.setTitle("Sistema de Gestão de Recursos Humanos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}