package wmg.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import wmg.domain.PerlinNoise;
import wmg.model.Vector2;

public class MainWindow {

    int width = 700;
    int height = 500;

    PerlinNoise pn = new PerlinNoise(this.width, this.height, 1);

    final BorderPane layout = new BorderPane();
    final Scene scene = new Scene(layout, this.width, this.height);

    final Canvas canvas = new Canvas(this.width, this.height - 100);
    final GraphicsContext gc = canvas.getGraphicsContext2D();

    final HBox bottomBar = new HBox();
    final Button generateButton = new Button("Generate");
    final Button vectorButton = new Button("Random Vector2");

    final EventHandler<ActionEvent> generateAction = (ActionEvent event) -> {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, this.width, this.height);
        Vector2 v2 = pn.randomVector();
        pn.generateGradients();
        gc.setFill(Color.WHITE);
        gc.fillText(
                v2.getY() + ", " + v2.getX(),
                Math.round(10),
                Math.round(canvas.getHeight() / 2)
        );
    };

    public MainWindow(Stage stage) {

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 700, 400);

        vectorButton.setOnAction(generateAction);
        bottomBar.getChildren().addAll(generateButton, vectorButton);
        layout.setBottom(bottomBar);
        layout.setTop(canvas);
    }

    public Scene getScene() {
        return scene;
    }

}
