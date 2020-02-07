package wmg.ui;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField {

    String dbl = "-?0\\.[0-9]+|-?1\\.0|-?1";
    String intgr = "[0-9]+";

    public NumberTextField(String string, boolean integer) {
        super(string);

        if (integer) {
            this.focusedProperty().addListener((arg0, oldValue, newValue) -> {
                if (!newValue) {
                    if (!this.getText().matches(intgr)) {
                        this.setText("0");
                    }
                }
            });
        } else {
            this.focusedProperty().addListener((arg0, oldValue, newValue) -> {
                if (!newValue) {
                    if (!this.getText().matches(dbl)) {
                        this.setText("0");
                    }
                }
            });
        }
    }
}
