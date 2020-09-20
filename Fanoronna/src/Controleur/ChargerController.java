package Controleur;

import Modele.*;
import fanorona.Fanorona;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import static javafx.application.Platform.exit;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class ChargerController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView btnParcourir;
    @FXML
    private TextField fenetreChoix;
    @FXML
    private ImageView btnAccueil;
    @FXML
    private ImageView btnQuitter;
    @FXML
    private ImageView btnCommencer;
    
    public Fanorona fn;
    public File selectedFile = null;
    public static Jeu jeu2;
    public Sauvegarde<Jeu> s;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fn= new Fanorona();
        s=new Sauvegarde<Jeu>();
        setToolTip();
    }    

    @FXML
    private void handleAccueil(MouseEvent event)throws Exception {
        fn.clic.play();
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("Vue/accueil.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void handleQuitter(MouseEvent event) {
        fn.clic.play();
        exit();
    }

    @FXML
    private void handleCommencer(MouseEvent event)throws Exception {
        fn.clic.play();
        jeu2 = s.load(selectedFile.getName());
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("Vue/plateau.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void handleParcourir(MouseEvent event) {
        fn.clic.play();
        FileChooser fc = new FileChooser();
        selectedFile = fc.showOpenDialog(null);
        fc.setInitialDirectory(new File("C:\\Users\\jilali\\Documents\\NetBeansProjects\\Fano\\src\\Sauvegarde"));
        if(selectedFile != null){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY hh:mm");
            Date dt = new Date(selectedFile.lastModified());
            String dateString = sdf.format(dt);
            fenetreChoix.setText(selectedFile.getName()+"    -   "+dateString);
        }else{
            System.out.println("File Not Found");
        }
    }
    
    public void setToolTip(){
        Tooltip.install(btnParcourir, new Tooltip("Parcourir"));
        Tooltip.install(btnAccueil, new Tooltip("Accueil"));
        Tooltip.install(btnCommencer, new Tooltip("Commencer"));
        Tooltip.install(btnQuitter, new Tooltip("Quitter"));
    } 

    
}
