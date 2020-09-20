package Modele;
import java.io.Serializable;


public class Mode implements Serializable{

    public static final int joueur_joueur       = 0;
    public static final int joueur_iaFacile     = 1;
    public static final int joueur_iaMoyen      = 2;
    public static final int joueur_iaDifficile  = 3;

    public int value;

    /*  CONSTRUCTEURS:
     *  Cree un Mode avec une valeur value.
     *  qui vaut 0 s'il y'a 2joueurs sinon 1 ou 2 ou 3 pour IA.
     */
    public Mode(int v){
        this.value = v;
    }

    /*Permet de recuperer la valeur value*/
    public int getMode(){
        return this.value;
    }

    /*met la valeur m a value*/
    public void setMode(int m){
        value = m;
    }

    public Mode clone(){
  		Mode copie = new Mode(this.value);
  		return copie;
  	}
}
