package wmg.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import wmg.domain.PerlinNoise;

public class MainWindow {

    int width = 700;
    int height = 500;

    final BorderPane layout = new BorderPane();
    final Scene scene = new Scene(layout, this.width, this.height);

    final Canvas canvas = new Canvas(this.width, this.height - 100);
    final GraphicsContext gc = canvas.getGraphicsContext2D();

    final Slider cellSlider = new Slider();
    final HBox sliderBox = makeSlider(cellSlider);
    final Button generateButton = new Button("Generate");
    final HBox bottomBar = new HBox(generateButton, sliderBox);

    final EventHandler<ActionEvent> generateAction = (ActionEvent event) -> {
        PerlinNoise pn = new PerlinNoise(this.width + 1, this.height + 1, (int) cellSlider.getValue());
        int[][] pixels = pn.getGrayscale();
        PixelWriter pw = gc.getPixelWriter();
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[0].length; x++) {
                pw.setColor(y, x, Color.rgb(pixels[y][x], pixels[y][x], pixels[y][x]));
            }
        }
    };

    public MainWindow(Stage stage) {

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 700, 400);

        generateButton.setOnAction(generateAction);

        bottomBar.setPadding(new Insets(10));

        layout.setBottom(bottomBar);
        layout.setTop(canvas);

    }

    private HBox makeSlider(Slider s) {
        Label label = new Label("Noise cell size: ");
        Text value = new Text();

        s.setMin(1);
        s.setMax(100);
        s.setValue(40);
        s.setShowTickMarks(true);
        s.setBlockIncrement(1);
        value.setText(Double.toString(s.getValue()));

        s.valueProperty().addListener(
                (observable, oldvalue, newvalue)
                -> {
            int i = newvalue.intValue();
            value.setText(Integer.toString(i));
        });

        HBox box = new HBox(label, s, value);
        box.setPadding(new Insets(10));

        return box;
    }

    public Scene getScene() {
        return scene;
    }

}
