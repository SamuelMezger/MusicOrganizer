package fx;


import javafx.application.Platform;
import javafx.stage.Stage;

public class NewMain {

    public static void main(String[] args) {
        
        HelloWorld view = new HelloWorld();

        try {
            view.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Platform.startup(() -> {
//            create primary stage
            Stage stage = new Stage();
            view.start(stage);
            Controller controller = new Controller(view);
        });
    }
}
