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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import wmg.domain.PerlinNoise;
import wmg.domain.DiamondSquare;

public class MainWindow {

    int width = 800;
    int height = 550;

    final BorderPane layout = new BorderPane();
    final Scene scene = new Scene(layout, this.width - 1, this.height);

    final Canvas canvas = new Canvas(this.width, this.height - 130);
    final GraphicsContext gc = canvas.getGraphicsContext2D();
    final PixelWriter pw = gc.getPixelWriter();

    final MenuBar menuBar = new MenuBar();
    final Menu menuFile = new Menu("File");
    final MenuItem menuFileSaveMap = new MenuItem("Save map");
    final VBox topMenu = new VBox(menuBar);

    final String perlinSelection = "Perlin noise";
    final String diamondSelection = "Diamond-square";
    final ObservableList<String> algorithms
            = FXCollections.observableArrayList(
                    perlinSelection,
                    diamondSelection
            );
    final ComboBox<String> algComboBox = new ComboBox<>(algorithms);

    final ToggleGroup modeGroup = new ToggleGroup();
    final RadioButton grayRadio = new RadioButton("Grayscale");
    final RadioButton terrainRadio = new RadioButton("Terrain");
    final HBox radioBox = new HBox(grayRadio, terrainRadio);
    final Label algLabel = new Label("Algorithm:");
    final Label modeLabel = new Label("Display: ");
    final VBox modeBox = new VBox(algLabel, algComboBox, modeLabel, radioBox);

    final Button generateButton = new Button("Generate");

    final Slider cellSlider = new Slider();
    final Slider octaveSlider = new Slider();
    final Slider attnSlider = new Slider();
    final Slider thresholdSlider = new Slider();
    final VBox sliderBox1 = new VBox();
    final VBox sliderBox2 = new VBox();

    final Label seedLabel = new Label("Map seed:");
    final NumberTextField seedText = new NumberTextField("3", true);
    final CheckBox fadeCheck = new CheckBox("Fade");
    final HBox checkBox = new HBox(fadeCheck);
    final VBox seedBox = new VBox();

    final Label cornerLabel = new Label("Corner values (in range [-1.0, 1.0]):");
    final CheckBox randomCheck = new CheckBox("Random");
    final NumberTextField tlNumber = new NumberTextField("0.2", false);
    final NumberTextField trNumber = new NumberTextField("0.3", false);
    final NumberTextField blNumber = new NumberTextField("0.1", false);
    final NumberTextField brNumber = new NumberTextField("0.4", false);
    final HBox topNumberBox = new HBox(tlNumber, trNumber, randomCheck);
    final HBox botNumberBox = new HBox(blNumber, brNumber);
    final VBox cornerBox = new VBox(cornerLabel, topNumberBox, botNumberBox);

    final HBox bottomBar = new HBox();

    // Generate a map or grayscale noise according to given parameters
    final EventHandler<ActionEvent> generateAction = (ActionEvent event) -> {
        int cHeight = (int) canvas.getHeight();
        int cWidth = (int) canvas.getWidth();
        double[][] pixels = new double[0][0];
        if (algComboBox.getValue().equals(perlinSelection)) {
            PerlinNoise pn = new PerlinNoise(cHeight, cWidth,
                    (int) cellSlider.getValue(),
                    (int) octaveSlider.getValue(),
                    attnSlider.getValue(),
                    fadeCheck.isSelected(),
                    Integer.parseInt(seedText.getText()));
            pixels = pn.getOctavedNoise();
        } else if (algComboBox.getValue().equals(diamondSelection)) {
            DiamondSquare ds;
            if (randomCheck.isSelected()) {
                ds = new DiamondSquare(cHeight, cWidth,
                        Integer.parseInt(seedText.getText()));
            } else {
                ds = new DiamondSquare(cHeight, cWidth,
                        Integer.parseInt(seedText.getText()),
                        Double.parseDouble(tlNumber.getText()),
                        Double.parseDouble(trNumber.getText()),
                        Double.parseDouble(blNumber.getText()),
                        Double.parseDouble(brNumber.getText())
                );
            }
            pixels = ds.getNoise();
        }
        double threshold = thresholdSlider.getValue();
        for (int y = 0; y < cHeight; y++) {
            for (int x = 0; x < cWidth; x++) {
                if ((RadioButton) modeGroup.getSelectedToggle() == grayRadio) {
                    int value = (int) (128 + 128 * pixels[y][x]);
                    pw.setColor(x, y, Color.rgb(value, value, value));
                } else {
                    double value = pixels[y][x];
                    if (value < threshold) {
                        pw.setColor(x, y, Color.BLUE);
                    } else if (value < threshold + 0.01) {
                        pw.setColor(x, y, Color.KHAKI);
                    } else if (value < threshold + 0.25) {
                        pw.setColor(x, y, Color.GREEN);
                    } else if (value < threshold + 0.35) {
                        pw.setColor(x, y, Color.DARKGREEN);
                    } else {
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

        perlinSetup();

        generateButton.setOnAction(generateAction);
        generateButton.setMinWidth(50);
        generateButton.setMinHeight(50);

        algComboBox.setValue(perlinSelection);
        algComboBox.setOnAction(e -> {
            if (algComboBox.getValue().equals(perlinSelection)) {
                perlinSetup();
            } else if (algComboBox.getValue().equals(diamondSelection)) {
                diamondSetup();
            }
        });

        randomCheck.setOnAction(t -> {
            toggleDiamondValues();
        });

        grayRadio.setToggleGroup(modeGroup);
        terrainRadio.setToggleGroup(modeGroup);
        terrainRadio.setSelected(true);

        topNumberBox.setSpacing(5);
        botNumberBox.setSpacing(5);

        seedBox.setPadding(new Insets(10));
        bottomBar.setPadding(new Insets(10));
        sliderBox1.setPadding(new Insets(10));
        sliderBox2.setPadding(new Insets(10));
        modeBox.setPadding(new Insets(10));
        cornerBox.setPadding(new Insets(10));

        bottomBar.setAlignment(Pos.CENTER_LEFT);

        menuFileSaveMap.setOnAction(exportAction);
        menuFile.getItems().addAll(menuFileSaveMap);
        menuBar.getMenus().add(menuFile);

        layout.setTop(topMenu);
        layout.setCenter(canvas);
        layout.setBottom(bottomBar);
    }

    private void perlinSetup() {
        int textWidth = 40;
        tlNumber.setPrefWidth(textWidth);
        trNumber.setPrefWidth(textWidth);
        blNumber.setPrefWidth(textWidth);
        brNumber.setPrefWidth(textWidth);

        sliderBox1.getChildren().clear();
        sliderBox1.getChildren().addAll(
                makeIntSliderBox(cellSlider, "Cell size: ", 2, 256, 100),
                makeIntSliderBox(octaveSlider, "Octaves: ", 1, 8, 4));

        sliderBox2.getChildren().clear();
        sliderBox2.getChildren().addAll(
                makeDoubleSliderBox(thresholdSlider, "Water threshold: ", -0.4, 0.5, -0.1, 0.01),
                makeDoubleSliderBox(attnSlider, "Attenuation: ", 0.1, 2, 0.5, 0.1));

        seedBox.getChildren().clear();
        seedBox.getChildren().addAll(seedLabel, seedText, checkBox);

        bottomBar.getChildren().clear();
        bottomBar.getChildren().addAll(generateButton, sliderBox1, sliderBox2, seedBox, modeBox);
    }

    private void diamondSetup() {
        sliderBox1.getChildren().clear();
        sliderBox1.getChildren().addAll(
                makeDoubleSliderBox(thresholdSlider, "Water threshold: ", -0.4, 0.5, 0.0, 0.01));

        randomCheck.setSelected(true);
        toggleDiamondValues();

        seedBox.getChildren().clear();
        seedBox.getChildren().addAll(seedLabel, seedText);

        bottomBar.getChildren().clear();
        bottomBar.getChildren().addAll(generateButton, cornerBox, sliderBox1, seedBox, modeBox);
    }

    private void toggleDiamondValues() {
        if (randomCheck.isSelected()) {
            tlNumber.setDisable(true);
            trNumber.setDisable(true);
            blNumber.setDisable(true);
            brNumber.setDisable(true);
            tlNumber.setText("?");
            trNumber.setText("?");
            blNumber.setText("?");
            brNumber.setText("?");
        } else {
            tlNumber.setDisable(false);
            trNumber.setDisable(false);
            blNumber.setDisable(false);
            brNumber.setDisable(false);
            tlNumber.setText("0");
            trNumber.setText("0");
            blNumber.setText("0");
            brNumber.setText("0");
        }
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
