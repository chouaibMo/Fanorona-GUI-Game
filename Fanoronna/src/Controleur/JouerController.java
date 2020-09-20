package Controleur;

import fanorona.Fanorona;
import java.net.URL;
import static javafx.application.Platform.exit;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class JouerController implements Initializable {
    ObservableList<String> choixJoueurList = FXCollections
        .observableArrayList("Joueur 1","Joueur 2", "IA Facile", "IA Moyenne", "IA Experte");

    @FXML
    private ImageView btnAccueil;
    @FXML
    private ImageView btnQuitter;
    @FXML
    private ImageView btnCommencer;
    @FXML
    private AnchorPane rootPane;
    @FXML
    public  ComboBox choixJoueur1,choixJoueur2;
    
    public static String name1;
    public static String name2;
    public Fanorona c;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        c = new Fanorona();
        choixJoueur1.setItems(choixJoueurList);
        choixJoueur2.setItems(choixJoueurList);
        
        choixJoueur1.setPromptText("Nom joueur");
        choixJoueur2.setPromptText("Nom joueur");
        setToolTip();
    }

    @FXML
    private void handleAccueil(MouseEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("Vue/accueil.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
        c.clic.play();
    }

    @FXML
    private void handleQuitter(MouseEvent event) {
        c.clic.play();
        exit();
    }

    @FXML
    private void handleCommencer(MouseEvent event) throws Exception {
        name1=(String)choixJoueur1.getEditor().getText();
        name2=(String)choixJoueur2.getEditor().getText();
        if(!name1.equals("") & !name2.equals("")){
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("Vue/plateau.fxml"));
            AnchorPane pane = loader.load();
            rootPane.getChildren().setAll(pane);
            c.clic.play();
        }  
        
    }
    
    public String getName(){
        return (String) choixJoueur2.getValue();
    }
    
    public void setToolTip(){
        Tooltip.install(btnAccueil, new Tooltip("Accueil"));
        Tooltip.install(btnQuitter, new Tooltip("Quitter"));
        Tooltip.install(btnCommencer, new Tooltip("Commencer"));
    }
    
}
