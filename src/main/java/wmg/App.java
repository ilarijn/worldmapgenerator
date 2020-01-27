package wmg;

import wmg.ui.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;

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
