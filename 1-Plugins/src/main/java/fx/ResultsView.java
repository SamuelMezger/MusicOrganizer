package fx;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ResultsView extends VBox {
    public ResultsView() {
//        this.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(3), new Insets(0, 0, 0, 0))));
        this.setPadding(new Insets(2, 0, 0, 0));
        this.setSpacing(3);
        this.addResult("asdf");
        this.addResult("asdfasdfasdfasdfadsf");
//        this.removeAllResults();
    }

    public void removeAllResults() {
        this.getChildren().removeAll(this.getChildren());
        this.getChildren().add(new Label("No Results"));
    }

    public void addResult(String title) {
        HBox resultPane = new HBox();
        resultPane.setSpacing(5);

        ImageView imageView = new ImageView(new Image(this.getClass().getResourceAsStream("/view/icons/image.png")));
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        resultPane.getChildren().add(imageView);

        resultPane.getChildren().add(new VBox(
                new Label(title),
                new Label("some Artist")
        ));

        Button result = new Button(null, resultPane);

        this.getChildren().add(result);
    }
}
