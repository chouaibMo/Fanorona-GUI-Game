package Controleur;

import static Controleur.ChargerController.jeu2;
import static fanorona.Fanorona.POPUPHEIGHT;
import static fanorona.Fanorona.POPUPWIDTH;
import static Controleur.AccueilController.*;
import Modele.*;
import Util.Couple;
import fanorona.Fanorona;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import static javafx.application.Platform.exit;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PlateauController implements Initializable,ChangeListener {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private AnchorPane plateau;
    private ImageView btnAccueil;
    @FXML
    private ImageView btnRestart;
    @FXML
    private ImageView btnRegles;
    @FXML
    private ImageView btnPause;
    private ImageView btnExit;
    @FXML
    private ImageView btnSave;
    @FXML
    private ImageView btnReprendre;
    @FXML
    private ImageView btnValider;
    @FXML
    private ImageView btnAnnuler;
    @FXML
    private ImageView btnRefaire;
    @FXML
    private Pane panePlateau;
    @FXML
    private Canvas canvas;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private DialogPane dialogPaneVictoire;
    @FXML
    private DialogPane dialogPanePause;
    @FXML
    private DialogPane dialogPaneRegles;
    @FXML
    private DialogPane dialogPaneSauvegarder;
    @FXML
    private Label nomJoueur;
    @FXML
    private ImageView btnParametre;
    @FXML
    private DialogPane dialogPaneParametre;
    @FXML
    private ImageView btnSound;
    @FXML
    private ImageView btnMusic;
    @FXML
    private Slider sliderSound;
    @FXML
    private Slider sliderMusic;
    @FXML
    private ImageView btnRetour;
    @FXML
    private ImageView btnRetourRegles;
    @FXML
    private ImageView btnJouer;
    public Fanorona fn;
    public double largeurCase;
    public double hauteurCase;
    public Jeu jeu;
    public HistoriqueJeu historique;
    public Sauvegarde<Jeu> s;
    public Joueur Joueur1,Joueur2,j;
    public boolean jouer, choix;
    public Noeud Narriver,Ndepart;
    public int direction,type;
    public BoxBlur blur;
    public GraphicsContext gc;
    public ImageLoader il;
    public ImageView imgJ2;
    public ImageView imgJ1;
    public ImageView imgVide;
    public ImageView imgJ2X;
    public ImageView imgJ1X;
    public ImageView imgJ1S;
    public ImageView imgJ2S;
    public ImageView imgP;
    public ImageView imgA;

    public double x,y;
    public boolean animationEnCours=false;
    public double progres, vitesse = 0.02;
    public double srcX, srcY, dstX, dstY;
    public double suppX,suppY;
    public boolean IaJoue=false;
    public ArrayList<Coup> listCoup;
    public int niemeCoup;
    public boolean IAExperte=false;
    public boolean IAMoyenne=false;
    public Coup cc = null;
    public int nbPionsManger=0;
    @FXML
    private TextField champSave;
    @FXML
    private ImageView btnValiderSave;
    @FXML
    private Label labelDone;
    @FXML
    private ImageView btnChoixJoueur;
    @FXML
    private ImageView btnExitVictoire;
    @FXML
    private ImageView btnreommencerPartieVictoire;
    @FXML
    private ToggleGroup assist;
    @FXML
    private RadioButton radioBtnOn;
    @FXML
    private RadioButton radioBtnOff;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fn = new Fanorona();
        il = new ImageLoader("/Ressources/");
        blur = new BoxBlur(4,4,4);
        gc = canvas.getGraphicsContext2D();
        s = new Sauvegarde<>();

        this.fn.media.setVolume(musicVolume);
        this.fn.clic.setVolume(soundVolume);
        sliderMusic.setValue(musicVolume);
        sliderSound.setValue(soundVolume);

        System.out.println(musicVolume+"    -   "+soundVolume);
        System.out.println(this.fn.media.getVolume()+"    -   "+this.fn.clic.getVolume());
        if(assisted) {
            radioBtnOn.setSelected(true);
            radioBtnOff.setSelected(false);
        }
        else         {
            radioBtnOn.setSelected(false);
            radioBtnOff.setSelected(true);
        }

        if(jeu2 != null){
            jeu = jeu2;

            label1.setText(jeu2.player1.name);
            label2.setText(jeu2.player2.name);
            changeColor2();
            System.out.println(jeu.jCourant.name);
        }else {
            Joueur1 = creerJoueur(JouerController.name1,1);
            Joueur2 = creerJoueur(JouerController.name2,2);
            label1.setText(Joueur1.name);
            label2.setText(Joueur2.name);
            jeu = new Jeu(Joueur1,Joueur2);
            changeColor();
        }

        if(jeu.jCourant.isRobot()){
            tourRobot();
        }

        historique = new HistoriqueJeu();
        historique.faire(jeu);

        imgJ2   = il.loadImageView("blue.png");
        imgJ1   = il.loadImageView("jaune.png");
        imgJ2X  = il.loadImageView("blueX.png");
        imgJ1X  = il.loadImageView("jauneX.png");
        imgJ1S  = il.loadImageView("jauneS.png");
        imgJ2S  = il.loadImageView("blueS.png");
        imgP  = il.loadImageView("parcouru.png");
        imgA  = il.loadImageView("accessible.png");

        setToolTip();
        dessinPlateau();

        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                fn.clic.play();
                int l,c,d;
                //Si le joueur doit choisir entre deux captures!
                if(choix){
                    dstX = Narriver.posY*(largeurCase*2);
                    dstY = Narriver.posX*(largeurCase*2);
                    Narriver = clicSouris(e.getX(), e.getY());
                    if(jeu.plateau.grille[Narriver.posX][Narriver.posY].value==jeu.jCourant.valPion){
                        Ndepart=(Noeud)Narriver.clone();
                        jeu.plateau.CLEARSucc();
                        jeu.plateau.setSucc(Ndepart.posX,Ndepart.posY);
                        dessinSelected(Ndepart);
                        jouer=true;
                        choix=false;
                    }
                    else{
                        l=Ndepart.posX;
                        c=Ndepart.posY;
                        d=direction;
                        if(Ndepart.Direction(Narriver)==-8) type = 1;
                        else type = 0;

                        if(jeu.depValid(l,c,d,type) && (nbPionsManger = jeu.coup(l,c,d,type))>= 0){
                            historique.faire(jeu);
                            srcX = c*(largeurCase*2);
                            srcY = l*(largeurCase*2);
                            suppX=srcX;
                            suppY=srcY;
                            animationEnCours = true;
                            progres = 0;
                            jeu.PionABouger = jeu.miseAjourPion(l,c,d);
                            jeu.CaseParcourue.add(new Couple(l,c));
                            jeu.PionABouger = jeu.miseAjourPion(l,c,d);
                            jeu.CaseParcourue.add(new Couple(l,c));
                        }

                        if(jeu.finJeu()){
                            nomJoueur.setText(jeu.winner);
                            dialogPaneVictoire.setVisible(true);
                            plateau.setEffect(blur);
                        }

                        choix=false;
                        jouer=false;
                        Narriver = null;
                        Ndepart = null;
                    }

                  //Si le joueur doit choisir le deplacement!

                }else if(jouer){
                      do{
                          Narriver = clicSouris(e.getX(), e.getY());
                      }while(Narriver == null);
                      direction = Ndepart.Direction(Narriver);
                      if(jeu.plateau.grille[Narriver.posX][Narriver.posY].value==jeu.jCourant.valPion){
                          Ndepart=(Noeud)Narriver.clone();
                          jeu.plateau.CLEARSucc();
                          jeu.plateau.setSucc(Ndepart.posX,Ndepart.posY);
                          dessinSelected(Ndepart);
                          jouer=true;
                      }else{
                            type = jeu.plateau.typeDeplacement(Ndepart.posX,Ndepart.posY,direction);
                            if(type==2){
                              croixDeplacement(Ndepart.posX,Ndepart.posY,direction);
                              choix = true;
                            }else{
                              l=Ndepart.posX;
                              c=Ndepart.posY;
                              d=direction;
                              if(jeu.depValid(l,c,d,type) && (nbPionsManger=jeu.coup(l,c,d,type))>=0){
                                  historique.faire(jeu);
                                  srcX = c*(largeurCase*2);
                                  srcY = l*(largeurCase*2);
                                  suppX=srcX;
                                  suppY=srcY;
                                  dstX = Narriver.posY*(largeurCase*2);
                                  dstY = Narriver.posX*(largeurCase*2);
                                  animationEnCours = true;
                                  progres = 0;
                                  jeu.PionABouger = jeu.miseAjourPion(l,c,d);
                                  jeu.CaseParcourue.add(new Couple(l,c));
                              }
                              jouer=false;
                            }
                            if(jeu.finJeu()){
                              nomJoueur.setText(jeu.winner);
                              dialogPaneVictoire.setVisible(true);
                              plateau.setEffect(blur);
                            }
                      }

                      //Si le joueur doit choisir le pion a deplacer!

                    }else{
                      do{
                          Ndepart = clicSouris(e.getX(), e.getY());
                      }while(Ndepart == null);
                      jeu.plateau.CLEARSucc();
                      jeu.plateau.setSucc(Ndepart.posX,Ndepart.posY);
                      if(jeu.plateau.grille[Ndepart.posX][Ndepart.posY].value==jeu.jCourant.valPion){
                        dessinSelected(Ndepart);
                        jouer=true;
                      }
                    }
            }
        });

        AnimationTimer anim1 = new AnimationTimer() {
            @Override
            public void handle(long now) {
              if (IaJoue == true) { // code if not connected
                  srcX = jeu.PionABouger.posY*(largeurCase*2);
                  srcY = jeu.PionABouger.posX*(largeurCase*2);
                  if(nbPionsManger > 0){
                      nbPionsManger = jeu.jCourant.secondMove();
                  }
                  if(nbPionsManger > 0){
                    suppX=srcX;
                    suppY=srcY;
                    dstX = jeu.PionABouger.posY*(largeurCase*2);
                    dstY = jeu.PionABouger.posX*(largeurCase*2);
                    animationEnCours = true;
                    progres = 0;
                    IaJoue=false;
                }else{
                    jeu.switchPlayer();
                    nbPionsManger = 0;
                    changeColor();
                    jeu.LastDirection = -1;
                    jeu.PionABouger = null;
                    jeu.initCaseP();
                    jouer=false;
                    choix=false;
                    IaJoue=false;
                    historique.faire(jeu);
                    dessinPlateau();

                    if(jeu.finJeu()){
                        nomJoueur.setText(jeu.winner);
                        dialogPaneVictoire.setVisible(true);
                        plateau.setEffect(blur);
                    }
                    else if(jeu.jCourant.isRobot()){
                        tourRobot();
                    }
                }
              }
            }
        };
        anim1.start();

        AnimationTimer anim = new AnimationTimer() {
          @Override
          public void handle(long now) {
              if (animationEnCours) {
                  progres += vitesse;
                  if (progres > 1) {
                      x = dstX;
                      y = dstY;
                      animationEnCours = false;
                      if(jeu.jCourant.name.equals("IA Facile")){
                          IaJoue = true;
                      }else if(jeu.jCourant.name.equals("IA Experte")){
                          IAExperte = true;
                      }else if(jeu.jCourant.name.equals("IA Moyenne")){
                          IAMoyenne = true;
                      }
                      dessinPlateau();
                  } else {

                      // Un petit effet d'accellération / ralentissement

                      double prop = (1 - Math.cos(progres * Math.PI)) / 2;
                      x =  Math.round((1 - prop) * srcX + prop * dstX);
                      y =  Math.round((1 - prop) * srcY + prop * dstY);
                  }
                  traceEnCours(jeu.jCourant.valPion,suppX,suppY,x,y);
                  suppX=x;
                  suppY=y;
              }
          }
      };
      anim.start();

      AnimationTimer animExperte = new AnimationTimer() {
            @Override
            public void handle(long now) {
                  if (IAExperte == true || IAMoyenne == true) {
                      if(niemeCoup < listCoup.size()){
                        cc = listCoup.get(niemeCoup);
                        nbPionsManger = jeu.jCourant.jouerCoup(cc);
                        jeu.CaseParcourue.add(new Couple(cc.posX,cc.posY));
                        jeu.plateau = cc.p;
                        srcX = cc.posY*(largeurCase*2);
                        srcY = cc.posX*(largeurCase*2);
                        suppX=srcX;
                        suppY=srcY;
                        Noeud n = jeu.miseAjourPion(cc.posX,cc.posY,cc.direction);
                        jeu.PionABouger = n;
                        dstX = n.posY*(largeurCase*2);
                        dstY = n.posX*(largeurCase*2);
                        animationEnCours = true;
                        progres = 0;
                        IAExperte=false;
                        IAMoyenne=false;
                        niemeCoup++;

                      }else{
                        jeu.switchPlayer();
                        nbPionsManger = 0;
                        changeColor();
                        jeu.LastDirection = -1;
                        jeu.PionABouger = null;
                        jeu.initCaseP();
                        jouer=false;
                        choix=false;
                        IAExperte=false;
                        IAMoyenne=false;
                        animationEnCours = false;
                        historique.faire(jeu);
                        dessinPlateau();

                        if(jeu.finJeu()){
                            nomJoueur.setText(jeu.winner);
                            dialogPaneVictoire.setVisible(true);
                            plateau.setEffect(blur);
                        }else if(jeu.jCourant.isRobot()){
                          tourRobot();
                        }
                    }
                }
            }
        };
        animExperte.start();
    }

    private void handleBtnAccueil(MouseEvent event) throws Exception {
        fn.clic.play();
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("Vue/accueil.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void handleBtnRestart(MouseEvent event) {
        fn.clic.play();
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        HBox hBoxButtons = new HBox();
        Text message = new Text("Voulez-vous vraiment recommencer la partie en cours ?");
        Text message2 = new Text("(Toute progression non sauvegardée sera perdue)");
        Button valider = new Button("Oui");
        Button annuler = new Button("Non");
        hBoxButtons.getChildren().addAll(annuler,valider);
        hBoxButtons.setSpacing(100);
        hBoxButtons.setAlignment(Pos.CENTER);
        dialogVbox.getChildren().addAll(message, message2);
        dialogVbox.getChildren().add(hBoxButtons);
        dialogVbox.setAlignment(Pos.CENTER);
        Scene dialogScene = new Scene(dialogVbox,POPUPWIDTH,POPUPHEIGHT);
        dialog.setScene(dialogScene);
        dialog.setTitle("PROG6 - Fanorona");
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("/Ressources/icon.png")));
        dialog.show();
        annuler.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                fn.clic.play();
                dialog.close();
            }
        });
        valider.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                fn.clic.play();
                dialog.close();
                jeu = new Jeu(Joueur1,Joueur2);
                changeColor();
                historique = new HistoriqueJeu();
                historique.faire(jeu);

                animationEnCours=false;
                vitesse = 0.02;
                IaJoue=false;
                IAExperte=false;
                IAMoyenne=false;
                cc = null;
                nbPionsManger=0;
                dessinPlateau();
                plateau.setEffect(null);
                dialogPanePause.setVisible(false);
                plateau.setDisable(false);
                dialogPaneSauvegarder.setVisible(false);
                jeu2=null;
                    }
        });
    }

    @FXML
    private void handleBtnRegles(MouseEvent event) throws Exception {
        fn.clic.play();
        plateau.setDisable(true);
        dialogPaneRegles.setVisible(true);
        plateau.setDisable(false);
        plateau.setEffect(blur);
    }

    @FXML
    private void handleBtnParametre(MouseEvent event) {
        fn.clic.play();
        dialogPaneParametre.setVisible(true);
        plateau.setDisable(true);
        plateau.setEffect(blur);
    }

    @FXML
    private void handleSound(MouseEvent event) {
    }

    @FXML
    private void handleMusic(MouseEvent event) {
    }


    @FXML
    private void handleRetour(MouseEvent event) {
        fn.clic.play();
        dialogPaneParametre.setVisible(false);
        plateau.setDisable(false);
        plateau.setEffect(null);
    }

    @FXML
    private void handleRetourRegles(MouseEvent event) {
        fn.clic.play();
        plateau.setDisable(false);
        dialogPaneRegles.setVisible(false);
        plateau.setEffect(null);
    }

    @FXML
    private void handleBtnPause(MouseEvent event) {
        fn.clic.play();
        plateau.setDisable(true);
        dialogPanePause.setVisible(true);
        plateau.setEffect(blur);
    }
    private void handleBtnExit(MouseEvent event) throws Exception {
        fn.clic.play();
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("Vue/jouer.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
        jeu2=null;
    }

    @FXML
    private void handleBtnSave(MouseEvent event) {
        fn.clic.play();
        dialogPaneSauvegarder.setVisible(true);
        labelDone.setVisible(false);
    }

    @FXML
    private void handleReprendre(MouseEvent event) {
        fn.clic.play();
        plateau.setDisable(false);
        dialogPanePause.setVisible(false);
        dialogPaneSauvegarder.setVisible(false);
        plateau.setEffect(null);
    }

    @FXML
    private void handleBtnAnnuler(MouseEvent event) {
        fn.clic.play();
        if(historique.passe.size()==1){
            jeu = (Jeu)historique.passe.getFirst().clone();
            jouer=false;
            choix=false;
        }else if(historique.peutAnnuler()){
            historique.annuler();
            jeu = (Jeu)historique.passe.getFirst().clone();
            jouer=false;
            choix=false;
        }
        dessinPlateau();
    }

    @FXML
    private void handleBtnRefaire(MouseEvent event) {
        fn.clic.play();
        if(historique.peutRefaire()){
            jeu = (Jeu)historique.futur.getFirst().clone();
            historique.refaire();
            jouer=false;
            choix=false;
            dessinPlateau();
        }
    }

    private void handleBtnHome(MouseEvent event) throws IOException {
        fn.clic.play();
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("Vue/accueil.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
        jeu2=null;
    }

    @FXML
    private void handlebtnValiderSave(MouseEvent event) {
        fn.clic.play();
        if(!champSave.equals("")){
            String message=s.save(champSave.getText(),(Jeu)jeu.clone());
            labelDone.setText(message);
            if(message.equals("echec de la sauvegarde"))
                labelDone.setTextFill(Color.RED);
            labelDone.setVisible(true);
        }
    }

    @FXML
    private void handleBtnValider(MouseEvent event) {
        fn.clic.play();
        jeu.switchPlayer();
        changeColor();
        System.out.println("Joueur courant : "+jeu.jCourant.name);
        jeu.LastDirection = -1;
        jeu.PionABouger = null;
        jeu.initCaseP();
        dessinPlateau();
        tourRobot();

        if(jeu.finJeu()){
            nomJoueur.setText(jeu.winner);
            dialogPaneVictoire.setVisible(true);
        }
    }

    @FXML
    private void handleBtnJouer(MouseEvent event)throws Exception {
        fn.clic.play();
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("Vue/jouer.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void handleBtnChoixJoueur(MouseEvent event)throws Exception {
        fn.clic.play();
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        HBox hBoxButtons = new HBox();
        Text message = new Text("Voulez-vous vraiment recommencer la partie en cours ?");
        Text message2 = new Text("(Toute progression non sauvegardée sera perdue)");
        Button valider = new Button("Oui");
        Button annuler = new Button("Non");
        hBoxButtons.getChildren().addAll(annuler,valider);
        hBoxButtons.setSpacing(100);
        hBoxButtons.setAlignment(Pos.CENTER);
        dialogVbox.getChildren().addAll(message, message2);
        dialogVbox.getChildren().add(hBoxButtons);
        dialogVbox.setAlignment(Pos.CENTER);
        Scene dialogScene = new Scene(dialogVbox,POPUPWIDTH,POPUPHEIGHT);
        dialog.setScene(dialogScene);
        dialog.setTitle("PROG6 - Fanorona");
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("/Ressources/icon.png")));
        dialog.show();
        annuler.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                fn.clic.play();
                dialog.close();
            }
        });
        valider.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    fn.clic.play();
                    dialog.close();
                    FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("Vue/jouer.fxml"));
                    AnchorPane pane = loader.load();
                    rootPane.getChildren().setAll(pane);
                } catch (IOException ex) {
                    Logger.getLogger(PlateauController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    @FXML
    private void handleExitVictoire(MouseEvent event) {
        fn.clic.play();
        exit();
    }

    @FXML
    private void handlereommencerPartieVictoire(MouseEvent event) {
        fn.clic.play();
        jeu = new Jeu(Joueur1,Joueur2);
        changeColor();

        historique = new HistoriqueJeu();
        historique.faire(jeu);

        animationEnCours=false;
        vitesse = 0.02;
        IaJoue=false;
        IAExperte=false;
        IAMoyenne=false;
        cc = null;
        nbPionsManger=0;
        dessinPlateau();
        plateau.setEffect(null);
        dialogPanePause.setVisible(false);
        plateau.setDisable(false);
        dialogPaneSauvegarder.setVisible(false);
        jeu2=null;
        dialogPaneVictoire.setVisible(false);
    }

    public Noeud clicSouris(double x, double y) {
        int l = (int) (y / hauteurCase());
        int c = (int) (x / largeurCase());
        l=(int)(l/2);
        c=(int)(c/2);
        if(l>=0 && l<5 && c>=0 && c<9) {
            Noeud n = jeu.plateau.grille[l][c];
            return n;
        }else return null;
    }

    private void dessinPlateau(){
        largeurCase=min(canvas.getHeight()/(jeu.plateau.LIGNE+5),canvas.getWidth()/(jeu.plateau.COLONNE+8));
        hauteurCase=min(canvas.getHeight()/(jeu.plateau.LIGNE+5),canvas.getWidth()/(jeu.plateau.COLONNE+8));

        gc.clearRect(0, 0, largeur(), hauteur());
        for(int i=0;i<jeu.CaseParcourue.size();i++){
            Couple couple = jeu.CaseParcourue.get(i);
            if(assisted)
                 gc.drawImage(imgP.getImage(), largeurCase*couple.y*2, largeurCase*couple.x*2, largeurCase, largeurCase);
        }
        for(int i = 0 ; i < jeu.plateau.LIGNE;i++){
            for(int j = 0 ; j < jeu.plateau.COLONNE;j++){

                //Dessin Les Chemins

                if(j==0)gc.strokeLine( largeurCase*j*2+(largeurCase/2),largeurCase*i*2+(largeurCase/2), largeurCase*8*2+(largeurCase/2),largeurCase*i*2+(largeurCase/2));
                if(i==0)gc.strokeLine( largeurCase*j*2+(largeurCase/2),largeurCase*i*2+(largeurCase/2), largeurCase*j*2+(largeurCase/2),largeurCase*4*2+(largeurCase/2));
                if((i+j)%2==0){
                    if ((i-1 >= 0)&&(j-1 >= 0)) gc.strokeLine( largeurCase*j*2, largeurCase*i*2,largeurCase*j*2-largeurCase,largeurCase*i*2-largeurCase);
                    if ((i-1 >= 0)&&(j+1 < jeu.plateau.COLONNE))gc.strokeLine(largeurCase*j*2+largeurCase,largeurCase*i*2,largeurCase*j*2+largeurCase*2,largeurCase*i*2-largeurCase);
                }

                //Dessin des noeuds

                if( jeu.plateau.grille[i][j].getValue()==1)gc.drawImage(imgJ1.getImage(), largeurCase*j*2, largeurCase*i*2, largeurCase, largeurCase);
                if( jeu.plateau.grille[i][j].getValue()==2)gc.drawImage(imgJ2.getImage(), largeurCase*j*2, largeurCase*i*2, largeurCase, largeurCase);
            }
        }
    }

    public void traceEnCours(int i,double xd,double yd,double xa,double ya){
        gc.clearRect(xd, yd, largeurCase, largeurCase);
        if(i==1) gc.drawImage(imgJ1.getImage(), xa, ya, largeurCase, largeurCase);
        if(i==2) gc.drawImage(imgJ2.getImage(), xa, ya, largeurCase, largeurCase);

    }

    public void croixDeplacement(int posX, int posY,int Direction){
        switch(Direction) {
            case 0:
            // "NORD";
                if(jeu.plateau.grille[posX][posY].value != 2){
                    gc.drawImage(imgJ2X.getImage(), largeurCase*posY*2, largeurCase*posX*2+2*largeurCase, largeurCase, largeurCase);
                    gc.drawImage(imgJ2X.getImage(), largeurCase*posY*2, largeurCase*posX*2-4*largeurCase, largeurCase, largeurCase);
                }else{
                    gc.drawImage(imgJ1X.getImage(), largeurCase*posY*2, largeurCase*posX*2+2*largeurCase, largeurCase, largeurCase);
                    gc.drawImage(imgJ1X.getImage(), largeurCase*posY*2, largeurCase*posX*2-4*largeurCase, largeurCase, largeurCase);
                }
                break;
            case 1:
            // "NORD EST";
                if(jeu.plateau.grille[posX][posY].value != 2){
                        gc.drawImage(imgJ2X.getImage(), largeurCase*posY*2-2*largeurCase, largeurCase*posX*2+2*largeurCase, largeurCase, largeurCase);
                        gc.drawImage(imgJ2X.getImage(), largeurCase*posY*2+4*largeurCase, largeurCase*posX*2-4*largeurCase, largeurCase, largeurCase);
                }else{
                        gc.drawImage(imgJ1X.getImage(), largeurCase*posY*2-2*largeurCase, largeurCase*posX*2+2*largeurCase, largeurCase, largeurCase);
                        gc.drawImage(imgJ1X.getImage(), largeurCase*posY*2+4*largeurCase, largeurCase*posX*2-4*largeurCase, largeurCase, largeurCase);
                }
                break;
            case 2:
                // "EST";
                if(jeu.plateau.grille[posX][posY].value != 2){
                    gc.drawImage(imgJ2X.getImage(), largeurCase*posY*2-2*largeurCase, largeurCase*posX*2, largeurCase, largeurCase);
                    gc.drawImage(imgJ2X.getImage(), largeurCase*posY*2+4*largeurCase, largeurCase*posX*2, largeurCase, largeurCase);
                }else{
                    gc.drawImage(imgJ1X.getImage(), largeurCase*posY*2-2*largeurCase, largeurCase*posX*2, largeurCase, largeurCase);
                    gc.drawImage(imgJ1X.getImage(), largeurCase*posY*2+4*largeurCase, largeurCase*posX*2, largeurCase, largeurCase);
                }
                break;
            case 3:
                // "SUD EST";
                if(jeu.plateau.grille[posX][posY].value != 2){
                    gc.drawImage(imgJ2X.getImage(), largeurCase*posY*2-2*largeurCase, largeurCase*posX*2-2*largeurCase, largeurCase, largeurCase);
                    gc.drawImage(imgJ2X.getImage(), largeurCase*posY*2+4*largeurCase, largeurCase*posX*2+4*largeurCase, largeurCase, largeurCase);
                }else{
                    gc.drawImage(imgJ1X.getImage(), largeurCase*posY*2-2*largeurCase, largeurCase*posX*2-2*largeurCase, largeurCase, largeurCase);
                    gc.drawImage(imgJ1X.getImage(), largeurCase*posY*2+4*largeurCase, largeurCase*posX*2+4*largeurCase, largeurCase, largeurCase);
                }
                break;
            case 4:
                // "SUD";
                if(jeu.plateau.grille[posX][posY].value != 2){
                    gc.drawImage(imgJ2X.getImage(), largeurCase*posY*2, largeurCase*posX*2-2*largeurCase, largeurCase, largeurCase);
                    gc.drawImage(imgJ2X.getImage(), largeurCase*posY*2, largeurCase*posX*2+4*largeurCase, largeurCase, largeurCase);
                }else{
                    gc.drawImage(imgJ1X.getImage(), largeurCase*posY*2, largeurCase*posX*2-2*largeurCase, largeurCase, largeurCase);
                    gc.drawImage(imgJ1X.getImage(), largeurCase*posY*2, largeurCase*posX*2+4*largeurCase, largeurCase, largeurCase);
                }
                break;
            case 5:
                // "SUD OUEST";
                if(jeu.plateau.grille[posX][posY].value != 2){
                    gc.drawImage(imgJ2X.getImage(), largeurCase*posY*2+2*largeurCase, largeurCase*posX*2-2*largeurCase, largeurCase, largeurCase);
                    gc.drawImage(imgJ2X.getImage(), largeurCase*posY*2-4*largeurCase, largeurCase*posX*2+4*largeurCase, largeurCase, largeurCase);
                }else{
                    gc.drawImage(imgJ1X.getImage(), largeurCase*posY*2+2*largeurCase, largeurCase*posX*2-2*largeurCase, largeurCase, largeurCase);
                    gc.drawImage(imgJ1X.getImage(), largeurCase*posY*2-4*largeurCase, largeurCase*posX*2+4*largeurCase, largeurCase, largeurCase);
                }
                break;
            case 6:
                // "OUEST";
                if(jeu.plateau.grille[posX][posY].value != 2){
                    gc.drawImage(imgJ2X.getImage(), largeurCase*posY*2+2*largeurCase, largeurCase*posX*2, largeurCase, largeurCase);
                    gc.drawImage(imgJ2X.getImage(), largeurCase*posY*2-4*largeurCase, largeurCase*posX*2, largeurCase, largeurCase);
                }else{
                    gc.drawImage(imgJ1X.getImage(), largeurCase*posY*2+2*largeurCase, largeurCase*posX*2, largeurCase, largeurCase);
                    gc.drawImage(imgJ1X.getImage(), largeurCase*posY*2-4*largeurCase, largeurCase*posX*2, largeurCase, largeurCase);
                }
                break;
            default:
                // "NORD OUEST";
                if(jeu.plateau.grille[posX][posY].value != 2){
                    gc.drawImage(imgJ2X.getImage(), largeurCase*posY*2+2*largeurCase, largeurCase*posX*2+2*largeurCase, largeurCase, largeurCase);
                    gc.drawImage(imgJ2X.getImage(), largeurCase*posY*2-4*largeurCase, largeurCase*posX*2-4*largeurCase, largeurCase, largeurCase);
                }else{
                    gc.drawImage(imgJ1X.getImage(), largeurCase*posY*2+2*largeurCase, largeurCase*posX*2+2*largeurCase, largeurCase, largeurCase);
                    gc.drawImage(imgJ1X.getImage(), largeurCase*posY*2-4*largeurCase, largeurCase*posX*2-4*largeurCase, largeurCase, largeurCase);
                }
            }
	}

    public Joueur creerJoueur(String s, int i){
        switch(s){
            case "IA Facile":
                j = new JoueurFacile(i);
                break;

            case "IA Moyenne":
                j = new JoueurMoyenne(i);
                break;

            case "IA Experte":
                j = new JoueurExperte(i);
                break;

            default:
                j = new JoueurHumain(s,i);
        }
        return j;
    }

    public void setToolTip(){
        Tooltip.install(btnAccueil, new Tooltip("Accueil"));
        Tooltip.install(btnExit, new Tooltip("Quitter"));
        Tooltip.install(btnReprendre, new Tooltip("Reprendre"));
        Tooltip.install(btnPause, new Tooltip("Pause"));
        Tooltip.install(btnRegles, new Tooltip("Regles"));
        Tooltip.install(btnValider, new Tooltip("Valider"));
        Tooltip.install(btnRestart, new Tooltip("Recommencer"));
        Tooltip.install(btnValiderSave, new Tooltip("Valider"));
        Tooltip.install(btnSave, new Tooltip("Sauvegarder"));
        Tooltip.install(btnChoixJoueur, new Tooltip("Jouer"));
    }

     public double largeur() {
        return canvas.getWidth();
    }

    public double hauteur() {
        return canvas.getHeight();
    }

    public double min(double l,double h){
      if(l>h) return h;
      return l;
    }

    public double largeurCase() {
    	return largeurCase;
    }

    public double hauteurCase() {
        return hauteurCase;
    }

    public void changeColor(){
        if(jeu.jCourant.equals(Joueur1)){
            label2.setDisable(true);
            label1.setDisable(false);
        }else{
            label2.setDisable(false);
            label1.setDisable(true);
        }
    }

    public void changeColor2(){
        if(jeu.jCourant.equals(jeu.player1)){
            label2.setDisable(true);
            label1.setDisable(false);
        }else{
            label2.setDisable(false);
            label1.setDisable(true);
        }
    }

    public void dessinSelected(Noeud n){
        dessinPlateau();
        jeu.plateau.CLEARSucc();
        jeu.plateau.setSucc(n.posX,n.posY);
        if(jeu.plateau.grille[n.posX][n.posY].value == 1){
            gc.drawImage(imgJ1S.getImage(), largeurCase*n.posY*2, largeurCase*n.posX*2, largeurCase, largeurCase);
        }else if(jeu.plateau.grille[n.posX][n.posY].value == 2){
            gc.drawImage(imgJ2S.getImage(), largeurCase*n.posY*2, largeurCase*n.posX*2, largeurCase, largeurCase);
        }
        for(int i=0;i<n.successeurs.size();i++){
            if(!jeu.contient(n.noeudDirec(n.successeurs.get(i)))){
              if(assisted)
                switch(n.successeurs.get(i)){
                    case 0:
                    gc.drawImage(imgA.getImage(), largeurCase*n.posY*2, largeurCase*n.posX*2-2*largeurCase, largeurCase, largeurCase);
                    break;
                    case 1:
                    gc.drawImage(imgA.getImage(), largeurCase*n.posY*2+2*largeurCase, largeurCase*n.posX*2-2*largeurCase, largeurCase, largeurCase);
                    break;
                    case 2:
                    gc.drawImage(imgA.getImage(), largeurCase*n.posY*2+2*largeurCase, largeurCase*n.posX*2, largeurCase, largeurCase);
                    break;
                    case 3:
                    gc.drawImage(imgA.getImage(), largeurCase*n.posY*2+2*largeurCase, largeurCase*n.posX*2+2*largeurCase, largeurCase, largeurCase);
                    break;
                    case 4:
                    gc.drawImage(imgA.getImage(), largeurCase*n.posY*2, largeurCase*n.posX*2+2*largeurCase, largeurCase, largeurCase);
                    break;
                    case 5:
                    gc.drawImage(imgA.getImage(), largeurCase*n.posY*2-2*largeurCase, largeurCase*n.posX*2+2*largeurCase, largeurCase, largeurCase);
                    break;
                    case 6:
                    gc.drawImage(imgA.getImage(), largeurCase*n.posY*2-2*largeurCase, largeurCase*n.posX*2, largeurCase, largeurCase);
                    break;
                    case 7:
                    gc.drawImage(imgA.getImage(), largeurCase*n.posY*2-2*largeurCase, largeurCase*n.posX*2-2*largeurCase, largeurCase, largeurCase);
                    break;
                }
            }
        }
    }

    public void tourRobot(){
        if(jeu.jCourant.name.equals("IA Facile")) {
            Coup cc = null;
            nbPionsManger=0;
            cc = jeu.jCourant.joue(jeu);
            if(cc != null) {
                nbPionsManger = jeu.jCourant.jouerCoup(cc);
            }
            srcX = cc.posY*(largeurCase*2);
            srcY = cc.posX*(largeurCase*2);
            suppX=srcX;
            suppY=srcY;
            dstX = jeu.miseAjourPion(cc.posX,cc.posY,cc.direction).posY*(largeurCase*2);
            dstY = jeu.miseAjourPion(cc.posX,cc.posY,cc.direction).posX*(largeurCase*2);
            animationEnCours = true;
            progres = 0;
        }else if(jeu.jCourant.name.equals("IA Moyenne")) {
                  listCoup = new ArrayList<Coup>();
                  listCoup.clear();
                  listCoup = jeu.jCourant.Bestcoup(jeu);
                  niemeCoup=0;
                  if(listCoup!=null){
                      IAMoyenne = true;
                  }
        }else if(jeu.jCourant.name.equals("IA Experte")) {
            nbPionsManger = 0;
            listCoup = new ArrayList<Coup>();
            listCoup.clear();
            listCoup = jeu.jCourant.Bestcoup(jeu);
            niemeCoup=0;
            if(listCoup!=null){
                IAExperte = true;
            }
        }
    }

    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        musicVolume = sliderMusic.getValue();
        soundVolume = sliderSound.getValue();
        fn.media.setVolume(musicVolume);
        fn.clic.setVolume(soundVolume);
    }

    @FXML
    private void handleRadioOff(MouseEvent event) {
        fn.clic.play();
        assisted = false;
    }

    @FXML
    private void handleradioOn(MouseEvent event) {
        fn.clic.play();
        assisted = true;
    }
}
