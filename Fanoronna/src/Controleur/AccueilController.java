package Controleur;

import fanorona.Fanorona;
import java.net.URL;
import java.util.ResourceBundle;
import static javafx.application.Platform.exit;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class AccueilController implements Initializable,ChangeListener {

    @FXML
    private ImageView btnSound;
    @FXML
    private ImageView btnMusic;
    @FXML
    private ImageView btnParametre;
    @FXML
    private ImageView btnJouer;
    @FXML
    private ImageView btnRegles;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView btnQuitter;
    @FXML
    private ImageView btnCharger;
    @FXML
    private DialogPane dialogPaneParametre;
    @FXML
    private Slider sliderSound;
    @FXML
    private Slider sliderMusic;
    @FXML
    private ImageView btnRetour;
    @FXML
    private AnchorPane accueil;
    public BoxBlur blur;
    @FXML
    private VBox vBoxParamQuitter;

    public static double soundVolume;
    public static double musicVolume;
    public static boolean assisted;

    public Fanorona fn;
    public ImageLoader il;
    public Image imgSoundOff;
    public Image imgSoundOn ;
    public Image imgMusicOff ;
    public Image imgMusicOn ;
    public boolean music;
    public boolean sound;
    @FXML
    private RadioButton radioBtnOn;
    @FXML
    private RadioButton radioBtnOff;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setToolTip();
        fn  = new Fanorona();
        sliderSound.valueProperty().addListener(this);
        sliderMusic.valueProperty().addListener(this);
        il = new ImageLoader("/Ressources/");
        imgSoundOff = il.loadImage("soundoff.png");
        imgSoundOn  = il.loadImage("sound.png");
        imgMusicOff = il.loadImage("musicoff.png");
        imgMusicOn  = il.loadImage("music.png");

        musicVolume = 0.5;
        soundVolume = 0.5;
        assisted    = true;
    }


    @FXML
    private void handleParametre(MouseEvent event) {
        fn.clic.play();
        dialogPaneParametre.setVisible(true);
        vBoxParamQuitter.setDisable(true);
    }

    @FXML
    private void handleJouer(MouseEvent event) throws Exception {
        fn.clic.play();
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("Vue/jouer.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void handleRegles(MouseEvent event) throws Exception {
        fn.clic.play();
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("Vue/regles.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void handleQuitter(MouseEvent event) {
        fn.clic.play();
        exit();
    }

    @FXML
    private void handleCharger(MouseEvent event) throws Exception {
        fn.clic.play();
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("Vue/charger.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }


    @FXML
    private void handleRetour(MouseEvent event) {
        fn.clic.play();
        dialogPaneParametre.setVisible(false);
        vBoxParamQuitter.setDisable(false);
    }

    @FXML
    private void clickedSound(MouseEvent event) {

    }

    @FXML
    private void clickedMusic(MouseEvent event) {

    }

    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        this.musicVolume = sliderMusic.getValue()/100;
        this.soundVolume = sliderSound.getValue()/100;
        System.out.println(musicVolume+"    -   "+soundVolume);
        fn.media.setVolume(musicVolume);
        fn.clic.setVolume(soundVolume);
    }

    public void setToolTip(){
        Tooltip.install(btnCharger, new Tooltip("Charger"));
        Tooltip.install(btnSound, new Tooltip("Son"));
        Tooltip.install(btnMusic, new Tooltip("Musique"));
        Tooltip.install(btnParametre, new Tooltip("Parametre"));
        Tooltip.install(btnJouer, new Tooltip("Jouer"));
        Tooltip.install(btnRegles, new Tooltip("Regles"));
        Tooltip.install(btnQuitter, new Tooltip("Quitter"));
    }

    @FXML
    private void handleRadioOn(MouseEvent event) {
        fn.clic.play();
        assisted = true;
    }

    @FXML
    private void handleRadioOff(MouseEvent event) {
        fn.clic.play();
        assisted = false;
    }
}
