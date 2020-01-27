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

    double threshold = -0.1;

    final BorderPane layout = new BorderPane();
    final Scene scene = new Scene(layout, this.width, this.height);

    final Canvas canvas = new Canvas(this.width, this.height - 100);
    final GraphicsContext gc = canvas.getGraphicsContext2D();
    final PixelWriter pw = gc.getPixelWriter();

    final Button generateButton = new Button("Generate");
    final Slider cellSlider = new Slider();
    final Slider octaveSlider = new Slider();
    final Slider thresholdSlider = new Slider();
    final VBox thBox = makeThresholdSlider(thresholdSlider);
    final VBox sliderBox = makeSliders(cellSlider, octaveSlider);
    final Label seedLabel = new Label("Map seed:");
    final TextField seedText = new TextField("50");
    final Label attnLabel = new Label("Attenuation:");
    final TextField attnText = new TextField("0.5");
    final CheckBox fadeCheck = new CheckBox("Fade");
    final CheckBox grayCheck = new CheckBox("Grayscale");
    final HBox checkBox = new HBox(fadeCheck, grayCheck);
    final VBox seedBox = new VBox(seedLabel, seedText, thBox);
    final VBox attnBox = new VBox(attnLabel, attnText, checkBox);
    final HBox bottomBar = new HBox(generateButton, sliderBox, attnBox, seedBox);

    // Generate a map or grayscale noise according to given parameters
    final EventHandler<ActionEvent> generateAction = (ActionEvent event) -> {
        int cHeight = (int) canvas.getHeight();
        int cWidth = (int) canvas.getWidth();
        PerlinNoise pn = new PerlinNoise(cHeight, cWidth,
                (int) cellSlider.getValue(),
                (int) octaveSlider.getValue(),
                Double.parseDouble(attnText.getText()),
                Integer.parseInt(seedText.getText()),
                fadeCheck.isSelected());
        double[][] pixels = pn.getOctavedNoise();
        for (int y = 0; y < cHeight; y++) {
            for (int x = 0; x < cWidth; x++) {
                if (grayCheck.isSelected()) {
                    int value = (int) (128 + 128 * pixels[y][x]);
                    pw.setColor(x, y, Color.rgb(value, value, value));
                } else {
                    double value = pixels[y][x];
                    if (value < threshold) {
                        pw.setColor(x, y, Color.BLUE);
                    } else if (value >= threshold && value < threshold + 0.02) {
                        pw.setColor(x, y, Color.YELLOW);
                    } else if (value >= threshold + 0.02 && value < threshold + 0.25) {
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
        attnBox.setPadding(new Insets(10));
        bottomBar.setPadding(new Insets(10));
        bottomBar.setAlignment(Pos.CENTER_LEFT);

        layout.setBottom(bottomBar);
        layout.setTop(canvas);
    }

    private VBox makeThresholdSlider(Slider th) {
        Label label = new Label("Water threshold: ");
        Text value = new Text();

        th.setMin(-0.4);
        th.setMax(0.5);
        th.setValue(-0.1);
        th.setShowTickMarks(true);
        th.setBlockIncrement(0.01);
        value.setText(Double.toString(th.getValue()));

        th.valueProperty().addListener((observable, oldvalue, newvalue)
                -> {
            double i = newvalue.doubleValue();
            threshold = i;
            value.setText(Double.toString(i));
        });

        HBox box = new HBox(label, value);
        return new VBox(box, th);
    }

    private VBox makeSliders(Slider cells, Slider octaves) {
        Label cLabel = new Label("Cell size: ");
        Text cValue = new Text();
        Label oLabel = new Label("Octaves: ");
        Text oValue = new Text();

        cells.setMin(1);
        cells.setMax(256);
        cells.setValue(40);
        cells.setShowTickMarks(true);
        cells.setBlockIncrement(1);
        cValue.setText(Integer.toString((int) cells.getValue()));

        cells.valueProperty().addListener((observable, oldvalue, newvalue)
                -> {
            int i = newvalue.intValue();
            cValue.setText(Integer.toString(i));
        });

        octaves.setMin(1);
        octaves.setMax(6);
        octaves.setValue(3);
        octaves.setShowTickMarks(true);
        octaves.setBlockIncrement(1);
        oValue.setText(Integer.toString((int) octaves.getValue()));

        octaves.valueProperty().addListener((observable, oldvalue, newvalue)
                -> {
            int i = newvalue.intValue();
            oValue.setText(Integer.toString(i));
        });

        HBox hbox1 = new HBox(cLabel, cValue);
        HBox hbox2 = new HBox(oLabel, oValue);
        VBox vbox = new VBox(hbox1, cells, hbox2, octaves);

        vbox.setPadding(new Insets(10));

        return vbox;
    }

    public Scene getScene() {
        return scene;
    }

}
