package fanorona;

import Modele.*;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import static javafx.scene.media.AudioClip.INDEFINITE;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class Fanorona extends Application {
    
    public static final double POPUPWIDTH = Screen.getPrimary().getVisualBounds().getWidth()*0.25;
    public static final double POPUPHEIGHT = Screen.getPrimary().getVisualBounds().getHeight()*0.25;
   
    public AudioClip media = new AudioClip(this.getClass().getResource("/Ressources/audio/track.wav").toExternalForm());
    public AudioClip clic = new AudioClip(this.getClass().getResource("/Ressources/audio/clic.wav").toExternalForm());
    public Sauvegarde <Jeu> s;
    
    
    @Override
    public void start(Stage stage) throws Exception {
        try{
            media.play();
            media.setCycleCount(INDEFINITE);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClassLoader.getSystemClassLoader().getResource("Vue/accueil.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            stage.getIcons().add(new Image(getClass().getResourceAsStream("/Ressources/icon.png")));
            stage.setTitle("PROG6 - Fanorona");
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
            
            scene.widthProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) -> {
                double newWidthDouble = newSceneWidth.doubleValue();
                TranslateTransition translate = new TranslateTransition(Duration.millis(1), root);
                translate.setToX((newWidthDouble-600)/1.8);
                ScaleTransition scale = new ScaleTransition(Duration.millis(1), root);
                scale.setToX(newWidthDouble/600);
                ParallelTransition transition = new ParallelTransition(root, scale, translate);
                transition.play();
            });
            scene.heightProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) -> {
                double newHeightDouble = newSceneHeight.doubleValue();
                TranslateTransition translate = new TranslateTransition(Duration.millis(1), root);
                translate.setToY((newHeightDouble-400)/1.8);
                ScaleTransition scale = new ScaleTransition(Duration.millis(1), root);
                scale.setToY(newHeightDouble/400);
                ParallelTransition transition = new ParallelTransition(root, scale, translate);
                transition.play();
            });
            
            
            Thread thread = new Thread(){
                @Override
                public void run() {
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run(){
                             stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                @Override
                                public void handle(WindowEvent t) {
                                    t.consume();
                                    final Stage dialog = new Stage();
                                    dialog.setTitle("PROG6 - Fanorona");
                                    dialog.getIcons().add(new Image(getClass().getResourceAsStream("/Ressources/icon.png")));
                                    dialog.initModality(Modality.APPLICATION_MODAL);        
                                    VBox dialogVbox = new VBox(20);
                                    HBox hBoxButtons = new HBox();
                                    Text message = new Text("Voulez-vous vraiment quitter le jeu ? ");    
                                    Text message2 = new Text("Toute progression non sauvegardée sera perdue");  
                                    Button valider = new Button("Oui");
                                    Button annuler = new Button("Non");
                                    hBoxButtons.getChildren().addAll(annuler,valider);
                                    hBoxButtons.setSpacing(100);
                                    hBoxButtons.setAlignment(Pos.CENTER);        
                                    dialogVbox.getChildren().addAll(message,message2);
                                    dialogVbox.getChildren().add(hBoxButtons);
                                    dialogVbox.setAlignment(Pos.CENTER);
                                    Scene dialogScene = new Scene(dialogVbox,POPUPWIDTH,POPUPHEIGHT);       
                                    dialog.setScene(dialogScene);
                                    dialog.show();
                                    annuler.setOnMouseClicked(new EventHandler<MouseEvent>() {            
                                        @Override
                                        public void handle(MouseEvent event) {
                                            dialog.close();
                                        }
                                    });        
                                    valider.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            dialog.close();
                                            stage.setOnCloseRequest(null);
                                            t.consume();
                                            Platform.exit();
                                        }
                                    });
                                }
                            });                        
                        }
                    });
                }                       
            };
            thread.start();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        Fanorona.launch(Fanorona.class,args);
    }
}
