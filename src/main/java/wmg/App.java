package wmg;

import javafx.application.Application;
import javafx.stage.Stage;
import wmg.ui.MainWindow;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        MainWindow mw = new MainWindow(stage);
        stage.setTitle("wmg");
        stage.setScene(mw.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
