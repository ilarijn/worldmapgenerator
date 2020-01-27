package wmg.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    final PixelWriter pw = gc.getPixelWriter();

    final Button generateButton = new Button("Generate");
    final Slider cellSlider = new Slider();
    final Slider octaveSlider = new Slider();
    final Slider thresholdSlider = new Slider();
    final Slider attnSlider = new Slider();
    final Label seedLabel = new Label("Map seed:");
    final TextField seedText = new TextField("50");
    final CheckBox fadeCheck = new CheckBox("Fade");
    final CheckBox grayCheck = new CheckBox("Grayscale");
    final HBox checkBox = new HBox(fadeCheck, grayCheck);
    final VBox seedBox = new VBox(seedLabel, seedText, checkBox);
    final VBox sliderBox1 = new VBox(
            makeIntSliderBox(cellSlider, "Cell size: ", 2, 256, 100),
            makeIntSliderBox(octaveSlider, "Octaves: ", 1, 6, 4));
    final VBox sliderBox2 = new VBox(
            makeDoubleSliderBox(thresholdSlider, "Water threshold: ", -0.4, 0.5, -0.1, 0.01),
            makeDoubleSliderBox(attnSlider, "Attenuation: ", 0.1, 2, 0.5, 0.1));
    final HBox bottomBar = new HBox(generateButton, sliderBox1, sliderBox2, seedBox);

    // Generate a map or grayscale noise according to given parameters
    final EventHandler<ActionEvent> generateAction = (ActionEvent event) -> {
        int cHeight = (int) canvas.getHeight();
        int cWidth = (int) canvas.getWidth();
        PerlinNoise pn = new PerlinNoise(cHeight, cWidth,
                (int) cellSlider.getValue(),
                (int) octaveSlider.getValue(),
                attnSlider.getValue(),
                Integer.parseInt(seedText.getText()),
                fadeCheck.isSelected());
        double[][] pixels = pn.getOctavedNoise();
        double threshold = thresholdSlider.getValue();
        for (int y = 0; y < cHeight; y++) {
            for (int x = 0; x < cWidth; x++) {
                if (grayCheck.isSelected()) {
                    int value = (int) (128 + 128 * pixels[y][x]);
                    pw.setColor(x, y, Color.rgb(value, value, value));
                } else {
                    double value = pixels[y][x];
                    if (value < threshold) {
                        pw.setColor(x, y, Color.BLUE);
                    } else if (value >= threshold && value < threshold + 0.01) {
                        pw.setColor(x, y, Color.YELLOW);
                    } else if (value >= threshold + 0.01 && value < threshold + 0.25) {
                        pw.setColor(x, y, Color.GREEN);
                    } else if (value >= threshold + 0.25 && value < threshold + 0.35) {
                        pw.setColor(x, y, Color.DARKGREEN);
                    } else if (value >= threshold + 0.35) {
                        pw.setColor(x, y, Color.GRAY);
                    }
                }
            }
        }
    };

    public MainWindow(Stage stage) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 700, 400);

        generateButton.setOnAction(generateAction);
        generateButton.setMinWidth(60);
        generateButton.setMinHeight(60);

        seedBox.setPadding(new Insets(10));
        bottomBar.setPadding(new Insets(10));
        sliderBox1.setPadding(new Insets(10));
        sliderBox2.setPadding(new Insets(10));

        bottomBar.setAlignment(Pos.CENTER_LEFT);

        layout.setBottom(bottomBar);
        layout.setTop(canvas);
    }

    private VBox makeIntSliderBox(Slider s, String l, int min, int max, int val) {
        Label label = new Label(l);
        Text value = new Text();

        s.setMin(min);
        s.setMax(max);
        s.setValue(val);
        s.setShowTickMarks(true);
        s.setBlockIncrement(1);
        value.setText(Integer.toString((int) s.getValue()));

        s.valueProperty().addListener((observable, oldvalue, newvalue)
                -> {
            int i = newvalue.intValue();
            value.setText(Integer.toString(i));
        });

        HBox hbox = new HBox(label, value);
        VBox vbox = new VBox(hbox, s);

        return vbox;
    }

    private VBox makeDoubleSliderBox(Slider s, String l, double min, double max, double val, double incr) {
        Label label = new Label(l);
        Text value = new Text();

        s.setMin(min);
        s.setMax(max);
        s.setValue(val);
        s.setShowTickMarks(true);
        s.setBlockIncrement(incr);
        value.setText(Double.toString(s.getValue()));

        s.valueProperty().addListener((observable, oldvalue, newvalue)
                -> {
            double i = newvalue.doubleValue();
            value.setText(Double.toString(i));
        });

        HBox hbox = new HBox(label, value);
        VBox vbox = new VBox(hbox, s);

        return vbox;
    }

    public Scene getScene() {
        return scene;
    }

}
