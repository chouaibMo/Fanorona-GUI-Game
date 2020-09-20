package Modele;
import Util.*;
import java.util.*;
import java.io.Serializable;


public class Plateau implements Serializable{

	public ConsoleColors c = new ConsoleColors();
	public Noeud[][] grille;
	public final int LIGNE = 5;
	public final int COLONNE = 9;

  /* Constructeur: initialise le plateau  */
	public Plateau(){
		boolean b=true;
		this.grille = new Noeud[LIGNE][COLONNE];
		for (int i = 0; i < LIGNE; i++) {
		   for (int j = 0; j < COLONNE; j++) {
				    if (i == 2 && j == 4)
				     			grille[i][j] = new Noeud(0,i,j);
				    else if (i < 2)
				     			grille[i][j] = new Noeud(2,i,j);
				    else if (i > 2)
				     			grille[i][j] = new Noeud(1,i,j);
				    else if (i == 2) {
						     if (b){
						      	grille[i][j] = new Noeud(1,i,j);
										b=false;
						     }else{
						      	grille[i][j] = new Noeud(2,i,j);
										b=true;
								 }
				    }
			}
		}
	}

	public Plateau(Noeud[][] g){
		this.grille = g;
	}


	public Plateau clone(){
		Plateau copie = new Plateau();

		for (int i = 0; i < LIGNE; i++) {
		   for (int j = 0; j < COLONNE; j++) {
				 		copie.grille[i][j] = (Noeud)this.grille[i][j].clone();
			 }
		}
		return copie;
	}

	public void setgrille(Plateau p){
		this.grille=p.grille;

	}

	/*Vide la liste des successeurs*/
	public void CLEARSucc(){
					for (int i = 0; i < LIGNE; i++) {
						 for (int j = 0; j < COLONNE; j++){grille[i][j].successeurs.clear();}
					}
	}

	/*Mets a jour la liste des successeurs de noeud (i,j)*/
	public void setSucc(int i,int j) {

							if (i+1 < LIGNE && grille[i+1][j].value==0)  grille[i][j].successeurs.add(Direction.S);
							if (i-1 >= 0 && grille[i-1][j].value==0)  grille[i][j].successeurs.add(Direction.N);
							if (j+1 < COLONNE && grille[i][j+1].value==0)  grille[i][j].successeurs.add(Direction.E);
							if (j-1 >= 0 && grille[i][j-1].value==0)  grille[i][j].successeurs.add(Direction.O);

							if((i+j)%2==0){
									if ((i-1 >= 0)&&(j-1 >= 0) && grille[i-1][j-1].value==0) grille[i][j].successeurs.add(Direction.NO);
									if ((i+1 < LIGNE)&&(j+1 < COLONNE) && grille[i+1][j+1].value==0) grille[i][j].successeurs.add(Direction.SE);
									if ((i-1 >= 0)&&(j+1  < COLONNE) && grille[i-1][j+1].value==0) grille[i][j].successeurs.add(Direction.NE);
									if ((i+1 < LIGNE)&&(j-1 >= 0)&& grille[i+1][j-1].value==0) grille[i][j].successeurs.add(Direction.SO);
							}
	}

	/*Renvoie le noeud de position(i,j)*/
	public Noeud getNoeud(int i,int j){
			return grille[i][j];
	}


	//Renvoie Vrai si le joueur j peut faire une capture!
	public Boolean PeutManger(Joueur p){
		boolean b;

		for (int i = 0; i < LIGNE; i++) {
		 for (int j = 0; j < COLONNE; j++) {
			if (grille[i][j].value == p.valPion){
					// "NORD";
					if((i-2>=0) && grille[i-1][j].value == 0 && grille[i-2][j].value!= 0 && grille[i-2][j].value!=grille[i][j].value) return true;
					if((i+1<LIGNE) &&(i-1>=0) && grille[i-1][j].value == 0 && grille[i+1][j].value!= 0 && grille[i+1][j].value!=grille[i][j].value) return true;

					// "SUD";
					if((i+2 < LIGNE) && grille[i+1][j].value == 0 && grille[i+2][j].value!= 0 && grille[i+2][j].value!=grille[i][j].value) return true;
					if((i-1 >=0) && (i+1<LIGNE) && grille[i+1][j].value == 0 && grille[i-1][j].value!= 0 && grille[i-1][j].value!=grille[i][j].value) return true;

					// "EST";
					if((j+2 < COLONNE) && grille[i][j+1].value == 0 && grille[i][j+2].value!= 0 && grille[i][j+2].value!=grille[i][j].value) return true;
					if((j-1 >=0) && (j+1 < COLONNE) && grille[i][j+1].value == 0 && grille[i][j-1].value!= 0 && grille[i][j-1].value!=grille[i][j].value) return true;

					// "OUEST";
					if((j-2 >= 0) && grille[i][j-1].value == 0 && grille[i][j-2].value!= 0 && grille[i][j-2].value!=grille[i][j].value) return true;
					if((j+1 < COLONNE) &&(j-1 >=0 ) &&  grille[i][j-1].value == 0 && grille[i][j+1].value!= 0 && grille[i][j+1].value!=grille[i][j].value)return true;


					if((i+j)%2==0){
					  //"NORD OUEST"
						if ((i-2 >= 0)&&(j-2 >= 0) && grille[i-1][j-1].value == 0 && grille[i-2][j-2].value!= 0 && grille[i-2][j-2].value!=grille[i][j].value) return true;
						if ((i+1<LIGNE)&&(j+1<COLONNE)&&(i-1>=0)&&(j-1>=0)&&grille[i-1][j-1].value==0&&grille[i+1][j+1].value!=0&&grille[i+1][j+1].value!=grille[i][j].value) return true;

						//"SUD EST"
						if ((i+2 < LIGNE)&&(j+2 < COLONNE) && grille[i+1][j+1].value==0 && grille[i+2][j+2].value!= 0 && grille[i+2][j+2].value!=grille[i][j].value) return true;
						if ((i-1 >=0)&&(j-1 >=0)&&(i+1< LIGNE)&&(j+1< COLONNE) && grille[i+1][j+1].value == 0 && grille[i-1][j-1].value!= 0 && grille[i-1][j-1].value!=grille[i][j].value)
							return true;

						//"NORD EST"
						if ((i-2 >= 0)&&(j+2  < COLONNE) && grille[i-1][j+1].value==0 && grille[i-2][j+2].value!= 0 && grille[i-2][j+2].value!=grille[i][j].value) return true;
						if ((i+1 <LIGNE)&&(j-1 >=0)&&(i-1>=0)&&(j+1< COLONNE) && grille[i-1][j+1].value == 0 && grille[i+1][j-1].value!= 0 && grille[i+1][j-1].value!=grille[i][j].value)
							return true;

						//"SUD OUEST"
						if ((i+2 < LIGNE)&&(j-2 >= 0)&& grille[i+1][j-1].value==0 && grille[i+2][j-2].value!= 0 && grille[i+2][j-2].value!=grille[i][j].value) return true;
						if ((i-1 >=0)&&(j+1< COLONNE)&&(i+1< LIGNE)&&(j-1>=0) && grille[i+1][j-1].value == 0 && grille[i-1][j+1].value!= 0 && grille[i-1][j+1].value!=grille[i][j].value)
							return true;
					}
			}
		 }
		}
		return false;
	}

	/*SI il existe deux types de captures(en s'eloignant et en s'approchant) renvoie 2
   *Si il existe que la capture en s'approchant renvoie 1
	 *Si il existe que la capture en s'eloignant renvoie 0
	 *Si aucune capture n'existe renvoie 0
	 */
	public int typeDeplacement(int posX, int posY,int Direction){
		switch(Direction) {
			case 0:
				// "NORD";
				if((posX-2>=0) && grille[posX-2][posY].value!=0 && grille[posX-2][posY].value != grille[posX][posY].value && (posX+1<LIGNE) && grille[posX+1][posY].value!=0 && grille[posX+1][posY].value != grille[posX][posY].value )
						return 2;
				if((posX-2>=0) && grille[posX-2][posY].value!=0 && grille[posX-2][posY].value != grille[posX][posY].value  )
						return 1;
				if((posX+1<LIGNE) && grille[posX+1][posY].value!=0 && grille[posX+1][posY].value != grille[posX][posY].value )
						return 0;

				break;
			case 1:
				// "NORD EST";
				if((posX-2>=0) && (posY+2<COLONNE) && (posX+1<LIGNE) && (posY-1>=0) && grille[posX-2][posY+2].value!=0 && grille[posX-2][posY+2].value!=grille[posX][posY].value&&grille[posX+1][posY-1].value!=0&&grille[posX+1][posY-1].value!=grille[posX][posY].value)
						return 2;
				if((posX-2>=0) && (posY+2<COLONNE) && grille[posX-2][posY+2].value!=0 && grille[posX-2][posY+2].value != grille[posX][posY].value  )
						return 1;
				if((posX+1<LIGNE) && (posY-1>=0) && grille[posX+1][posY-1].value!=0 && grille[posX+1][posY-1].value != grille[posX][posY].value )
						return 0;

				break;
			case 2:
				// "EST";
				if((posY-1>=0) && grille[posX][posY-1].value!=0 && grille[posX][posY-1].value != grille[posX][posY].value && (posY+2<COLONNE) && grille[posX][posY+2].value!=0 && grille[posX][posY+2].value != grille[posX][posY].value )
						return 2;
				if((posY+2<COLONNE) && grille[posX][posY+2].value!=0 && grille[posX][posY+2].value != grille[posX][posY].value)
						return 1;
				if((posY-1>=0) && grille[posX][posY-1].value!=0 && grille[posX][posY-1].value != grille[posX][posY].value)
						return 0;

				break;
			case 3:
				if((posX+2<LIGNE) && (posY+2<COLONNE) && (posX-1>=0) && (posY-1>=0) && grille[posX+2][posY+2].value!=0 && grille[posX+2][posY+2].value != grille[posX][posY].value && grille[posX-1][posY-1].value!=0 && grille[posX-1][posY-1].value != grille[posX][posY].value )
						return 2;
				if((posX+2<LIGNE) && (posY+2<COLONNE)&& grille[posX+2][posY+2].value!=0 && grille[posX+2][posY+2].value != grille[posX][posY].value  )
						return 1;
				if((posX-1>=0) && (posY-1>=0)  && grille[posX-1][posY-1].value!=0 && grille[posX-1][posY-1].value != grille[posX][posY].value )
						return 0;

				break;
			case 4:
				if((posX-1>=0) && grille[posX-1][posY].value!=0 && grille[posX-1][posY].value != grille[posX][posY].value && (posX+2<LIGNE) && grille[posX+2][posY].value!=0 && grille[posX+2][posY].value != grille[posX][posY].value )
						return 2;
				if((posX-1>=0) && grille[posX-1][posY].value!=0 && grille[posX-1][posY].value != grille[posX][posY].value  )
						return 0;
				if((posX+2<LIGNE) && grille[posX+2][posY].value!=0 && grille[posX+2][posY].value != grille[posX][posY].value )
						return 1;
				break;
			case 5:
				// "SUD OUEST";
				if((posX+2<LIGNE) && (posY-2>=0) && (posX-1>=0) && (posY+1<COLONNE) && grille[posX+2][posY-2].value!=0 && grille[posX+2][posY-2].value != grille[posX][posY].value && grille[posX-1][posY+1].value!=0 && grille[posX-1][posY+1].value != grille[posX][posY].value )
						return 2;
				if((posX+2<LIGNE) && (posY-2>=0)&& grille[posX+2][posY-2].value!=0 && grille[posX+2][posY-2].value != grille[posX][posY].value  )
						return 1;
				if((posX-1>=0) && (posY+1<COLONNE) && grille[posX-1][posY+1].value!=0 && grille[posX-1][posY+1].value != grille[posX][posY].value )
						return 0;

				break;
			case 6:
				// "OUEST";
				if((posY+1<COLONNE) && grille[posX][posY+1].value!=0 && grille[posX][posY+1].value != grille[posX][posY].value && (posY-2>=0) && grille[posX][posY-2].value!=0 && grille[posX][posY-2].value != grille[posX][posY].value )
						return 2;
				if((posY-2>=0) && grille[posX][posY-2].value!=0 && grille[posX][posY-2].value != grille[posX][posY].value  )
						return 1;
				if((posY+1<COLONNE) && grille[posX][posY+1].value!=0 && grille[posX][posY+1].value != grille[posX][posY].value)
						return 0;

				break;
			default:
				// "NORD OUEST";
				if((posX-2>=0) && (posY-2>=0) && (posX+1<LIGNE) && (posY+1<COLONNE) && grille[posX-2][posY-2].value!=0 && grille[posX-2][posY-2].value != grille[posX][posY].value && grille[posX+1][posY+1].value!=0 && grille[posX+1][posY+1].value != grille[posX][posY].value )
						return 2;
				if((posX-2>=0) && (posY-2>=0)&& grille[posX-2][posY-2].value!=0 && grille[posX-2][posY-2].value != grille[posX][posY].value  )
						return 1;
				if((posX+1<LIGNE) && (posY+1<COLONNE) && grille[posX+1][posY+1].value!=0 && grille[posX+1][posY+1].value != grille[posX][posY].value )
						return 0;

	}
	return 0;//pas necessaire!
}

	//Renvoie Vrai s'il y'a une capture dans ce deplacement
	public Boolean DeplacePionAvecCapture(int posX, int posY,int Direction){
		switch(Direction) {
			case 0:
				// "NORD";
				if((posX-1>=0) && grille[posX-1][posY].value == 0){
							if((posX-2>=0) && grille[posX-2][posY].value!=0 && grille[posX-2][posY].value != grille[posX][posY].value ) return true;
							if((posX+1<LIGNE) && grille[posX+1][posY].value!=0 && grille[posX+1][posY].value != grille[posX][posY].value ) return true;
				}

				break;
			case 1:
				// "NORD EST";
				if((posX-1 >= 0) && (posY+1 < COLONNE) && grille[posX-1][posY+1].value == 0){
							if((posX-2 >= 0) && (posY+2 < COLONNE) && grille[posX-2][posY+2].value!=0 && grille[posX-2][posY+2].value != grille[posX][posY].value ) return true;
							if((posX+1 < LIGNE) && (posY-1 >= 0) && grille[posX+1][posY-1].value!=0 && grille[posX+1][posY-1].value != grille[posX][posY].value ) return true;
				}

				break;
			case 2:
				// "EST";
				if((posY+1 < COLONNE) && grille[posX][posY+1].value == 0){
							if((posY+2 < COLONNE) && grille[posX][posY+2].value!=0 && grille[posX][posY+2].value != grille[posX][posY].value ) return true;
							if((posY-1>=0) && grille[posX][posY-1].value!=0 && grille[posX][posY-1].value != grille[posX][posY].value ) return true;
				}

				break;
			case 3:
				// "SUD EST";
				if((posX+1 < LIGNE) && (posY+1 < COLONNE) && grille[posX+1][posY+1].value == 0){
							if((posX+2 < LIGNE) && (posY+2 < COLONNE) && grille[posX+2][posY+2].value!=0 && grille[posX+2][posY+2].value != grille[posX][posY].value ) return true;
							if((posX-1 >=0) && (posY-1 >=0) && grille[posX-1][posY-1].value!=0 && grille[posX-1][posY-1].value != grille[posX][posY].value ) return true;
				}

				break;
			case 4:
				// "SUD";
				if((posX+1 < LIGNE) && grille[posX+1][posY].value == 0){
							if((posX+2 < LIGNE) && grille[posX+2][posY].value!=0 && grille[posX+2][posY].value != grille[posX][posY].value ) return true;
							if((posX-1 >=0) && grille[posX-1][posY].value!=0 && grille[posX-1][posY].value != grille[posX][posY].value ) return true;
				}

				break;
			case 5:
				// "SUD OUEST";
				if((posX+1<LIGNE) && (posY-1>=0) && grille[posX+1][posY-1].value == 0){
							if((posX+2 < LIGNE) && (posY-2 >= 0) && grille[posX+2][posY-2].value!=0 && grille[posX+2][posY-2].value != grille[posX][posY].value )return true;
							if((posX-1>=0) && (posY+1<COLONNE) && grille[posX-1][posY+1].value!=0 && grille[posX-1][posY+1].value != grille[posX][posY].value ) return true;
				}

				break;
			case 6:
				// "OUEST";
				if((posY-1>= 0) && grille[posX][posY-1].value == 0){
							if((posY-2>= 0) && grille[posX][posY-2].value!=0 && grille[posX][posY-2].value != grille[posX][posY].value ) return true;
							if((posY+1< COLONNE) && grille[posX][posY+1].value!=0 && grille[posX][posY+1].value != grille[posX][posY].value ) return true;
				}

				break;
			default:
				// "NORD OUEST";
				if((posX-1>=0) && (posY-1>=0) && grille[posX-1][posY-1].value == 0){
							if((posX-2>=0) && (posY-2>=0) && grille[posX-2][posY-2].value!=0 && grille[posX-2][posY-2].value != grille[posX][posY].value ) return true;
							if((posX+1<LIGNE) && (posY+1<COLONNE) && grille[posX+1][posY+1].value!=0 && grille[posX+1][posY+1].value != grille[posX][posY].value ) return true;
				}
		}
		return false;
	}


	/* Fait le deplacement de pion (posX,posY) vers la Direction
	 * en capturant tous les pions possibles a cette direction
	 * renvoie le nombres de pions captures
	 */
	public int DeplacePionApproche(int posX, int posY,int Direction){
			int depX;
			int depY;
			int nb=0;
			setSucc(posX,posY);
			if (!grille[posX][posY].successeurs.contains(Direction)) {
				System.out.println("Deplacement illegal.");
				return -1;
                        }else{
					switch(Direction) {
						case 0:
							// "NORD";
							depX = posX-1;
							if((depX>=0) && grille[depX][posY].value == 0){
										grille[depX][posY].setValue(grille[posX][posY].value);
										depX--;
										while((depX>=0) && grille[depX][posY].value!=0 && grille[depX][posY].value != grille[posX][posY].value ){
											grille[depX][posY].setValue(0);
											nb++;
											depX--;
										}
							}
							break;
						case 1:
							// "NORD EST";
							depX = posX-1;
							depY = posY+1;
							if((depX >= 0) && (depY < COLONNE) && grille[depX][depY].value == 0){
										grille[depX][depY].setValue(grille[posX][posY].value);
										depX--;
										depY++;
										while((depX >= 0) && (depY < COLONNE) && grille[depX][depY].value!=0 && grille[depX][depY].value != grille[posX][posY].value ){
											grille[depX][depY].setValue(0);
											nb++;
											depX--;
											depY++;
										}
							}

							break;
						case 2:
							// "EST";
							depY = posY+1;
							if((depY < COLONNE) && grille[posX][depY].value == 0){
										grille[posX][depY].setValue(grille[posX][posY].value);
										depY++;
										while((depY < COLONNE) && grille[posX][depY].value!=0 && grille[posX][depY].value != grille[posX][posY].value ){
											grille[posX][depY].setValue(0);
											nb++;
											depY++;
										}
							}

							break;
						case 3:
							// "SUD EST";
							depX = posX+1;
							depY = posY+1;
							if((depX < LIGNE) && (depY < COLONNE) && grille[depX][depY].value == 0){
										grille[depX][depY].setValue(grille[posX][posY].value);
										depX++;
										depY++;
										while((depX < LIGNE) && (depY < COLONNE) && grille[depX][depY].value!=0 && grille[depX][depY].value != grille[posX][posY].value ){
											grille[depX][depY].setValue(0);
											nb++;
											depX++;
											depY++;
										}
							}

							break;
						case 4:
							// "SUD";
							depX = posX+1;
							if((depX < LIGNE) && grille[depX][posY].value == 0){
										grille[depX][posY].setValue(grille[posX][posY].value);
										depX++;
										while((depX < LIGNE) && grille[depX][posY].value!=0 && grille[depX][posY].value != grille[posX][posY].value ){
											grille[depX][posY].setValue(0);
											nb++;
											depX++;
										}
							}

							break;
						case 5:
							// "SUD OUEST";
							depX = posX+1;
							depY = posY-1;
							if((depX < LIGNE) && (depY >= 0) && grille[depX][depY].value == 0){
										grille[depX][depY].setValue(grille[posX][posY].value);
										depX++;
										depY--;
										while((depX < LIGNE) && (depY >= 0) && grille[depX][depY].value!=0 && grille[depX][depY].value != grille[posX][posY].value ){
											grille[depX][depY].setValue(0);
											nb++;
											depX++;
											depY--;
										}
							}

							break;
						case 6:
							// "OUEST";
							depY = posY-1;
							if((depY >= 0) && grille[posX][depY].value == 0){
										grille[posX][depY].setValue(grille[posX][posY].value);
										depY--;
										while((depY >= 0) && grille[posX][depY].value!=0 && grille[posX][depY].value != grille[posX][posY].value ){
											grille[posX][depY].setValue(0);
											nb++;
											depY--;
										}
							}

							break;
						default:
							// "NORD OUEST";
							depX = posX-1;
							depY = posY-1;
							if((depX>=0) && (depY>=0) && grille[depX][depY].value == 0){
										grille[depX][depY].setValue(grille[posX][posY].value);
										depX--;
										depY--;
										while((depX>=0) && (depY>=0) && grille[depX][depY].value!=0 && grille[depX][depY].value != grille[posX][posY].value ){
											grille[depX][depY].setValue(0);
											nb++;
											depX--;
											depY--;
										}
							}

					}
					grille[posX][posY].setValue(0);
					return nb;
	  	}
	}

	/* Fait le deplacement de pion (posX,posY) vers la Direction
	 * en capturant tous les pions possibles a l'autre direction
	 * renvoie le nombres de pions captures
	 */
	public int DeplacePionEloigner(int posX, int posY,int Direction){
			int depX;
			int depY;
			int nb =0;
			setSucc(posX,posY);
			if (!grille[posX][posY].successeurs.contains(Direction)) {
					System.out.println("Deplacement illegal.");
					return -1;
		  }else{
					switch(Direction) {
						case 0:
							// "NORD";
							depX = posX-1;
							if((depX>=0) && grille[depX][posY].value == 0){
										grille[depX][posY].setValue(grille[posX][posY].value);
										depX = posX+1;
										while((depX < LIGNE) && grille[depX][posY].value!=0 && grille[depX][posY].value != grille[posX][posY].value ){
											grille[depX][posY].setValue(0);
											nb++;
											depX++;
										}
							}

							break;
						case 1:
							// "NORD EST";
							depX = posX-1;
							depY = posY+1;
							if((depX >= 0) && (depY < COLONNE) && grille[depX][depY].value == 0){
										grille[depX][depY].setValue(grille[posX][posY].value);
									  depX = posX+1;
									  depY = posY-1;
										while((depX < LIGNE) && (depY >= 0) && grille[depX][depY].value!=0 && grille[depX][depY].value != grille[posX][posY].value ){
											grille[depX][depY].setValue(0);
											nb++;
											depX++;
											depY--;
										}
							}

							break;
						case 2:
							// "EST";
							depY = posY+1;
							if((depY < COLONNE) && grille[posX][depY].value == 0){
										grille[posX][depY].setValue(grille[posX][posY].value);
										depY = posY-1;
										while((depY >= 0) && grille[posX][depY].value!=0 && grille[posX][depY].value != grille[posX][posY].value ){
											grille[posX][depY].setValue(0);
											nb++;
											depY--;
										}
							}

							break;
						case 3:
							// "SUD EST";
							depX = posX+1;
							depY = posY+1;
							if((depX < LIGNE) && (depY < COLONNE) && grille[depX][depY].value == 0){
										grille[depX][depY].setValue(grille[posX][posY].value);
										depX = posX-1;
										depY = posY-1;
										while((depX>=0) && (depY>=0) && grille[depX][depY].value!=0 && grille[depX][depY].value != grille[posX][posY].value ){
											grille[depX][depY].setValue(0);
											nb++;
											depX--;
											depY--;
										}
							}

							break;
						case 4:
							// "SUD";
							depX = posX+1;
							if((depX < LIGNE) && grille[depX][posY].value == 0){
										grille[depX][posY].setValue(grille[posX][posY].value);
										depX = posX-1;
										while((depX>=0) && grille[depX][posY].value!=0 && grille[depX][posY].value != grille[posX][posY].value ){
											grille[depX][posY].setValue(0);
											nb++;
											depX--;
										}
							}

							break;
						case 5:
							// "SUD OUEST";
							depX = posX+1;
							depY = posY-1;
							if((depX < LIGNE) && (depY >= 0) && grille[depX][depY].value == 0){
										grille[depX][depY].setValue(grille[posX][posY].value);
										depX = posX-1;
										depY = posY+1;
										while((depX >= 0) && (depY < COLONNE) && grille[depX][depY].value!=0 && grille[depX][depY].value != grille[posX][posY].value ){
											grille[depX][depY].setValue(0);
											nb++;
											depX--;
											depY++;
										}
							}

							break;
						case 6:
							// "OUEST";
							depY = posY-1;
							if((depY >= 0) && grille[posX][depY].value == 0){
										grille[posX][depY].setValue(grille[posX][posY].value);
										depY = posY+1;
										while((depY < COLONNE) && grille[posX][depY].value!=0 && grille[posX][depY].value != grille[posX][posY].value ){
											grille[posX][depY].setValue(0);
											nb++;
											depY++;
										}
							}

							break;
						default:
							// "NORD OUEST";
							depX = posX-1;
							depY = posY-1;
							if((depX>=0) && (depY>=0) && grille[depX][depY].value == 0){
										grille[depX][depY].setValue(grille[posX][posY].value);
										depX = posX+1;
										depY = posY+1;
										while((depX < LIGNE) && (depY < COLONNE) && grille[depX][depY].value!=0 && grille[depX][depY].value != grille[posX][posY].value ){
											grille[depX][depY].setValue(0);
											nb++;
											depX++;
											depY++;
										}
							}

					}
					grille[posX][posY].setValue(0);
					return nb;
		}
	}
/*--------------------------------------Version sur terminal------------------------------------------*/


	public void afficher(){
	  	System.out.println(" +-----------+-----------------------------+");
			System.out.println(" |    axes   |  0  1  2  3  4  5  6  7  8  |");
			System.out.println(" +-----------+-----------------------------+");
			for (int i = 0; i < LIGNE; i++) {
				System.out.print(" |     "+i+"     |  ");
			   for (int j = 0; j < COLONNE; j++) {
					 if (grille[i][j].value==1) System.out.print(c.col("X" +"  ",c.GREEN_B));
					 if (grille[i][j].value==2) System.out.print(c.col("O" +"  ",c.RED_B));
					 if (grille[i][j].value==0) System.out.print(c.col("-" +"  ",c.WHITE_B));
				}
				System.out.println("|");
			}
			System.out.println(" +-----------+-----------------------------+");
			System.out.println("");
		}

}
