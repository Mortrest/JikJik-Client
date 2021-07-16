package client.Controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class FollowerComponentController {
    @FXML
    public Pane pane;
    @FXML
    public Rectangle rect,rect1;
    @FXML
    private Label name;
    @FXML
    private FontAwesomeIconView deleteSign;
    public Label getName() {
        return name;
    }

    public void setName(String str) {
        name.setText(str);
    }

    public FontAwesomeIconView getDeleteSign() {
        return deleteSign;
    }

    public Pane getPane() {
        return pane;
    }

    public Rectangle getRect() {
        return rect;
    }
}
