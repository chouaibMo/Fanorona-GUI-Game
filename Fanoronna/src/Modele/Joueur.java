package Modele;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Joueur implements Serializable{

	public String name;      //nom du joueur
	public int value;        // score ou parties gagnees .......
	public int valPion;      // valeur de pion
	public int nbPions;			 // nombres de pions restants.

	/*Renvoie le nom de joueur*/
	public String getName(){
		return this.name;
	}

	/*mets s comme nom de joueur*/
	public void setName(String s){
		this.name=s;
	}

	/*joue le coup c*/
	public abstract int jouerCoup(Coup c);
	//public abstract Joueur clone();
	/*Renvoie un coup possible pour le joueur*/
	public Coup joue(Jeu j){
		return null;
	}

	public int secondMove(){return 0;}
	public Coup coupNormal(Jeu j){return null;}

	public Joueur clone(){
		Joueur copie = this;
		return copie;
	}
	
        public ArrayList<Coup> Bestcoup(Jeu j){return null;}
        
        public boolean isRobot(){
            return (this.name=="IA Facile" || this.name=="IA Moyenne" || this.name=="IA Experte");
        }

}
