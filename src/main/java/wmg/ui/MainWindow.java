package wmg.ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainWindow {

    private final BorderPane layout = new BorderPane();
    private final Scene scene = new Scene(layout, 450, 350);
    private final HBox bottomBar = new HBox();
    private final Button applyButton = new Button("Apply");

    public MainWindow(Stage stage) {
        bottomBar.getChildren().add(applyButton);
        layout.setBottom(bottomBar);
    }

    public Scene getScene() {
        return scene;
    }

}
