package wmg.ui;

import javafx.scene.control.TextField;

public class DoubleTextField extends TextField {

    public DoubleTextField(String string) {
        super(string);

        this.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                if (!this.getText().matches("-?0\\.[0-9]+|-?1\\.0|-?1")) {
                    this.setText("0");
                }
            }
        });
    }
}
