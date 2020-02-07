package wmg.ui;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField {

    public NumberTextField(String string, boolean integer) {
        super(string);

        String regex = integer ? "[0-9]+" : "-?0\\.[0-9]+|-?1\\.0|-?1";

        this.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                if (!this.getText().matches(regex)) {
                    this.setText("0");
                }
            }
        });
    }
}
