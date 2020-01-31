package wmg.ui;

import java.awt.image.RenderedImage;
import java.io.File;
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
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;

import wmg.domain.PerlinNoise;

public class MainWindow {

    int width = 725;
    int height = 500;

    final BorderPane layout = new BorderPane();
    final Scene scene = new Scene(layout, this.width - 1, this.height);

    final Canvas canvas = new Canvas(this.width, this.height - 100);
    final GraphicsContext gc = canvas.getGraphicsContext2D();
    final PixelWriter pw = gc.getPixelWriter();

    final Button generateButton = new Button("Generate");
    final Button exportButton = new Button("Export canvas");
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
            makeIntSliderBox(octaveSlider, "Octaves: ", 1, 8, 4));
    final VBox sliderBox2 = new VBox(
            makeDoubleSliderBox(thresholdSlider, "Water threshold: ", -0.4, 0.5, -0.1, 0.01),
            makeDoubleSliderBox(attnSlider, "Attenuation: ", 0.1, 2, 0.5, 0.1));
    final VBox exportBox = new VBox(exportButton);
    final HBox bottomBar = new HBox(generateButton, sliderBox1, sliderBox2, seedBox, exportBox);

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

    // Save canvas contents to a .png file
    final EventHandler<ActionEvent> exportAction = (ActionEvent event) -> {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage newStage = new Stage();
        File file = fileChooser.showSaveDialog(newStage);

        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                canvas.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    };

    public MainWindow(Stage stage) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        exportButton.setOnAction(exportAction);
        generateButton.setOnAction(generateAction);
        generateButton.setMinWidth(50);
        generateButton.setMinHeight(50);

        seedBox.setPadding(new Insets(10));
        bottomBar.setPadding(new Insets(10));
        sliderBox1.setPadding(new Insets(10));
        sliderBox2.setPadding(new Insets(10));
        exportBox.setPadding(new Insets(30));

        exportBox.setAlignment(Pos.BOTTOM_CENTER);
        bottomBar.setAlignment(Pos.CENTER_LEFT);

        layout.setBottom(bottomBar);
        layout.setTop(canvas);
    }

    private VBox makeIntSliderBox(Slider s, String l, int min, int max, int val) {
        Label label = new Label(l);
        Text value = new Text();
        HBox hbox = new HBox(label, value);
        VBox vbox = new VBox(hbox, s);

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

        return vbox;
    }

    private VBox makeDoubleSliderBox(Slider s, String l, double min, double max, double val, double incr) {
        Label label = new Label(l);
        Text value = new Text();
        HBox hbox = new HBox(label, value);
        VBox vbox = new VBox(hbox, s);

        s.setMin(min);
        s.setMax(max);
        s.setValue(val);
        s.setShowTickMarks(true);
        s.setBlockIncrement(incr);
        value.setText(Double.toString(s.getValue()));

        s.valueProperty().addListener((observable, oldvalue, newvalue)
                -> {
            double i = newvalue.doubleValue();
            value.setText(String.format("%.3f", i));
        });

        return vbox;
    }

    public Scene getScene() {
        return scene;
    }

}
