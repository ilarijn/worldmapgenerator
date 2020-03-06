package wmg.ui;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import wmg.domain.DiamondSquare;
import wmg.domain.PerlinNoise;
import wmg.domain.Rivers;
import wmg.util.Random;

public class MainWindow {

    int width = 1000;
    int height = 750;

    
    /************************************
     * UI COMPONENTS                    * 
     ************************************/
    
    final BorderPane root = new BorderPane();
    final Scene scene = new Scene(root, this.width - 1, this.height);

    final Canvas canvas = new Canvas(this.width, this.height - 150);
    final GraphicsContext gc = canvas.getGraphicsContext2D();
    final PixelWriter pw = gc.getPixelWriter();

    final MenuBar menuBar = new MenuBar();
    final Menu menuFile = new Menu("File");
    final MenuItem menuFileSaveMap = new MenuItem("Save map");
    final Text coordinates = new Text();
    final VBox topMenu = new VBox(menuBar, coordinates);

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
    final VBox algBox = new VBox(algLabel, algComboBox);
    final VBox displayBox = new VBox(modeLabel, radioBox);
    final VBox modeBox = new VBox(algBox, displayBox);

    final Button generateButton = new Button("Generate");

    final Slider cellSlider = new Slider();
    final Slider octaveSlider = new Slider();
    final Slider ampSlider = new Slider();
    final Slider waterSlider = new Slider();
    final Slider riverSlider = new Slider();
    final VBox sliderBox1 = new VBox();
    final VBox sliderBox2 = new VBox();
    final VBox sliderBox3 = new VBox(makeIntSliderBox(riverSlider, "Rivers: ", 0, 5, 0));

    final Label seedLabel = new Label("Map seed:");
    final NumberTextField seedText = new NumberTextField("3", true);
    final VBox seedTextBox = new VBox(seedLabel, seedText);
    final VBox seedBox = new VBox(seedTextBox, sliderBox3);

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

    
    /************************************
     * EVENT HANDLERS                   * 
     ************************************/
    
    // Generate a map or grayscale noise according to given parameters
    final EventHandler<ActionEvent> generateAction = (ActionEvent event) -> {
        int cHeight = (int) canvas.getHeight();
        int cWidth = (int) canvas.getWidth();
        double[][] pixels = new double[0][0];
        int seed = Integer.parseInt(seedText.getText());

        if (algComboBox.getValue().equals(perlinSelection)) {
            PerlinNoise pn = new PerlinNoise(cHeight, cWidth,
                    (int) cellSlider.getValue(),
                    (int) octaveSlider.getValue(),
                    ampSlider.getValue(),
                    seed);
            pixels = pn.getOctavedNoise();

        } else if (algComboBox.getValue().equals(diamondSelection)) {
            DiamondSquare ds;

            // Grid size must be 2^n+1, the power of 2 greater to or equal than the 
            // larger of the given height and width values + 1.
            int size = cHeight > cWidth ? cHeight : cWidth;
            size = (1 << (int) Math.ceil(Math.log(size) / Math.log(2))) + 1;

            if (randomCheck.isSelected()) {
                ds = new DiamondSquare(size,
                        Integer.parseInt(seedText.getText()));
            } else {
                ds = new DiamondSquare(size,
                        Integer.parseInt(seedText.getText()),
                        Double.parseDouble(tlNumber.getText()),
                        Double.parseDouble(trNumber.getText()),
                        Double.parseDouble(blNumber.getText()),
                        Double.parseDouble(brNumber.getText()));
            }
            pixels = ds.getNoise();
        }

        Rivers r = new Rivers(pixels);
        Random rand = new Random(seed);
        int amount = (int) riverSlider.getValue();

        for (int i = 0; i < amount; i++) {
            int srcY = (int) (rand.nextDouble() * cHeight - 50);
            int srcX = (int) (rand.nextDouble() * cWidth - 50);

            int destY = (int) (rand.nextDouble() * cHeight + 50);
            int destX = (int) (rand.nextDouble() * cWidth + 50);

            srcY = srcY < cHeight ? 0 : srcY;
            srcX = srcX < cWidth ? 0 : srcX;

            destY = destY > cHeight ? cHeight - 1 : destY;
            destX = destX > cWidth ? cWidth - 1 : destX;

            r.apply(srcY, srcX, destY, destX, waterSlider.getValue());
        }

        double threshold = waterSlider.getValue();
        boolean gray = (RadioButton) modeGroup.getSelectedToggle() == grayRadio;

        for (int y = 0; y < cHeight; y++) {
            for (int x = 0; x < cWidth; x++) {

                if (gray) {
                    int value = (int) (128 + 128 * pixels[y][x]);
                    pw.setColor(x, y, Color.rgb(value, value, value));
                } else {
                    double value = pixels[y][x];
                    if (value <= threshold) {
                        pw.setColor(x, y, Color.BLUE);
                    } else if (value < threshold + 0.01) {
                        pw.setColor(x, y, Color.KHAKI);
                    } else if (value < threshold + 0.25) {
                        pw.setColor(x, y, Color.GREEN);
                    } else if (value < threshold + 0.35) {
                        pw.setColor(x, y, Color.DARKGREEN);
                    } else if (value < threshold + 0.60) {
                        pw.setColor(x, y, Color.GRAY);
                    } else {
                        pw.setColor(x, y, Color.ANTIQUEWHITE);
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
                WritableImage writableImage = new WritableImage(
                        (int) canvas.getWidth(), (int) canvas.getHeight());
                canvas.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    };

    // Set algorithm-appropriate elements
    final EventHandler<ActionEvent> algAction = (ActionEvent event) -> {
        if (algComboBox.getValue().equals(perlinSelection)) {
            perlinSetup();
        } else if (algComboBox.getValue().equals(diamondSelection)) {
            diamondSetup();
        }
    };

    // Mouse coordinates
    final EventHandler<MouseEvent> coordEvent = (MouseEvent event) -> {
        coordinates.setText(
                "y: "
                + (int) (event.getSceneY() - topMenu.getHeight())
                + " x: " + (int) event.getSceneX());
    };

    
    /***********************************
     * LAYOUT AND COMPONENT PROPERTIES * 
     ***********************************/
    
    public MainWindow(Stage stage) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        perlinSetup();

        generateButton.setOnAction(generateAction);
        algComboBox.setOnAction(algAction);
        menuFileSaveMap.setOnAction(exportAction);
        randomCheck.setOnAction(t -> {
            toggleDiamondValues();
        });

        canvas.setOnMouseMoved(coordEvent);

        algComboBox.setValue(perlinSelection);

        grayRadio.setToggleGroup(modeGroup);
        terrainRadio.setToggleGroup(modeGroup);
        terrainRadio.setSelected(true);

        generateButton.setMinWidth(50);
        generateButton.setMinHeight(50);

        topNumberBox.setSpacing(5);
        botNumberBox.setSpacing(5);
        seedBox.setSpacing(0);
        radioBox.setSpacing(5);
        modeBox.setSpacing(5);

        seedBox.setPadding(new Insets(10, 0, 0, 0));
        bottomBar.setPadding(new Insets(10));
        sliderBox1.setPadding(new Insets(10));
        sliderBox2.setPadding(new Insets(10));
        modeBox.setPadding(new Insets(10));
        cornerBox.setPadding(new Insets(10));

        bottomBar.setAlignment(Pos.CENTER_LEFT);

        menuFile.getItems().addAll(menuFileSaveMap);
        menuBar.getMenus().addAll(menuFile);

        root.setTop(topMenu);
        root.setCenter(canvas);
        root.setBottom(bottomBar);
    }

    private void perlinSetup() {
        int textWidth = 40;
        tlNumber.setPrefWidth(textWidth);
        trNumber.setPrefWidth(textWidth);
        blNumber.setPrefWidth(textWidth);
        brNumber.setPrefWidth(textWidth);

        sliderBox1.getChildren().clear();
        sliderBox1.getChildren().addAll(
                makeIntSliderBox(cellSlider, "Scale: ", 2, 256, 100),
                makeIntSliderBox(octaveSlider, "Octaves: ", 1, 8, 5));

        sliderBox2.getChildren().clear();
        sliderBox2.getChildren().addAll(
                makeDoubleSliderBox(waterSlider, "Water level: ", -0.4, 0.5, -0.1, 0.01),
                makeDoubleSliderBox(ampSlider, "Amplitude: ", 0.1, 2, 0.6, 0.1));

        bottomBar.getChildren().clear();
        bottomBar.getChildren().addAll(generateButton, sliderBox1, sliderBox2, seedBox, modeBox);
    }

    private void diamondSetup() {
        sliderBox1.getChildren().clear();
        sliderBox1.getChildren().addAll(
                makeDoubleSliderBox(waterSlider, "Water level: ", -0.4, 0.5, 0.0, 0.01));

        randomCheck.setSelected(true);
        toggleDiamondValues();

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

        s.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            int i = newvalue.intValue();
            value.setText(Integer.toString(i));
        });

        return vbox;
    }

    private VBox makeDoubleSliderBox(
            Slider s, String l, double min, double max, double val, double incr) {
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

        s.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            double i = newvalue.doubleValue();
            value.setText(String.format("%.3f", i));
        });

        return vbox;
    }

    public Scene getScene() {
        return scene;
    }

}
