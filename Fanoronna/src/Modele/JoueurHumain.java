package Modele;
import java.io.Serializable;


public class JoueurHumain extends Joueur implements Serializable{

	/*  CONSTRUCTEURS:
	 *  Cree un Joueur avec name s et valPion i.
	 */
	public JoueurHumain(String s, int i){
		this.name = s;
		this.value = 0;
		this.nbPions = 22;
		this.valPion = i;
	}



	/* deplace le pion (posX,posY) vers la direction d selon le type de coup c */
	public int jouerCoup(Coup c){
		int v;
		switch(c.type){
				case 1:
						v= c.p.DeplacePionApproche(c.posX,c.posY,c.direction);
						break;
				case 0:
						v= c.p.DeplacePionEloigner(c.posX,c.posY,c.direction);
						break;
				default:
						v=-1;
		}
		return v;
	}
}
