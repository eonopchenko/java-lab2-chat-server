import java.net.URL;
import java.util.Optional;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ClientMain extends Application {
    public void start(Stage primaryStage) throws Exception {

    	TextInputDialog dialog = new TextInputDialog("walter");
    	dialog.setTitle("Nickname Input");
    	dialog.setHeaderText("");
    	dialog.setContentText("Please enter your nickname:");

    	Optional<String> result = dialog.showAndWait();
    	if (result.isPresent()) {
    	    System.out.println("Your name: " + result.get());
        	URL fxmlUrl = this.getClass().getClassLoader().getResource("ClientLayout.fxml");
            Pane mainPane = FXMLLoader.<Pane>load(fxmlUrl);
            primaryStage.setTitle("Chat Client");
            primaryStage.setScene(new Scene(mainPane));
            primaryStage.show();
    	}
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}