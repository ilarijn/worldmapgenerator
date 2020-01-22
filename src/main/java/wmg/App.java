package wmg;

import wmg.ui.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("wmg");
        MainWindow mw = new MainWindow(primaryStage);
        primaryStage.setScene(mw.getScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
