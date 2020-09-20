package Controleur;

import java.net.URL;
import java.util.ResourceBundle;
import static javafx.application.Platform.exit;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ReglesController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView btnCommencer;
    @FXML
    private ImageView btnRetour;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setToolTip();
    }    

    @FXML
    private void handleCommencer(MouseEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("Vue/jouer.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void handleRetour(MouseEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("Vue/accueil.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }
    
    public void setToolTip(){
        Tooltip.install(btnRetour, new Tooltip("Accueil"));
        Tooltip.install(btnCommencer, new Tooltip("Commencer"));
    }
}
