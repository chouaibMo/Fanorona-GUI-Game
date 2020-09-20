package Modele;
import Util.*;

import	java.util.*;
import java.io.Serializable;
import java.util.ArrayList;


public class JoueurExperte extends Joueur implements Serializable{

    public int profondeur = 6;
	public final int coeffPionsManges = 5;
	public final int coeffPositionPions = 1;
	public final int MAX = 1000;
	public final int MIN = -1000;
	public  ArrayList<Noeud> PionsEnAction;
	public  ArrayList<ArrayList<Integer>> PionsEnActionDeplacement;
	public  ArrayList<Integer> directionP;
	public  ArrayList<Coup> coupCap;
	public  ArrayList<Coup> coupNCap;
	public  Jeu jeu;

	int Max = -100;

	ArrayList<Coup> listeSol;
	int nbPionsM=0;
	public  ArrayList<Couple> casesParcourue;
	public  int Ldirection;



	/*  CONSTRUCTEURS:
	 *  Cree un Joueur avec valPion i.
	 */
	public JoueurExperte(int i){
		this.name = "IA Experte";
		this.value = 0;
		this.nbPions = 22;
		this.valPion = i;

		this.PionsEnAction= new ArrayList<Noeud>();// Contient les noueds qui peuvent se deplacer en capture ou sans
		this.PionsEnActionDeplacement= new ArrayList<ArrayList<Integer>>(); //Contient une liste d'entiers (direction possibles)
		this.directionP= new ArrayList<Integer>();// liste d'entiers (directions)
		this.coupCap=new ArrayList<Coup>();//Contient les triples (posX,posY,direction) ou il y'a au moins une capture
		this.coupNCap=new ArrayList<Coup>();//Contient les triples (posX,posY,direction) ou il y'a pas de captures
		this.casesParcourue=new ArrayList<Couple>();
	}

	//Rempli les 2 listes coupCap et coupNCap
	public void RemplirList(Plateau p){
		this.PionsEnAction.clear();
		this.PionsEnActionDeplacement.clear();
		this.directionP.clear();
		this.coupCap.clear();
	  this.coupNCap.clear();
		int type;
		Coup pt;
		p.CLEARSucc();
		for (int i = 0; i < p.LIGNE; i++) {
		   for (int j = 0; j < p.COLONNE; j++) {
				 p.setSucc(i,j);
				 	if(p.grille[i][j].value==this.valPion){
							if(p.grille[i][j].successeurs.size()!=0){
									PionsEnAction.add(p.grille[i][j]);
									PionsEnActionDeplacement.add(p.grille[i][j].successeurs);

							}
					}
			 }
		 }
		 for (int x = 0; x < PionsEnAction.size(); x++) {
				ArrayList<Integer> listdirection = PionsEnActionDeplacement.get(x);
				for (int y = 0; y < listdirection.size(); y++) {

                                    if(p.DeplacePionAvecCapture(PionsEnAction.get(x).posX,PionsEnAction.get(x).posY,listdirection.get(y))){
                                            type = p.typeDeplacement(PionsEnAction.get(x).posX, PionsEnAction.get(x).posY,listdirection.get(y));
                                            if(type == 0){
                                                    pt=new Coup(PionsEnAction.get(x).posX,PionsEnAction.get(x).posY,p,listdirection.get(y),0);
                                                    coupCap.add(pt);
                                            }else if(type == 1){
                                                    pt=new Coup(PionsEnAction.get(x).posX,PionsEnAction.get(x).posY,p,listdirection.get(y),1);
                                                    coupCap.add(pt);
                                            }else if(type == 2){
                                                    pt=new Coup(PionsEnAction.get(x).posX,PionsEnAction.get(x).posY,p,listdirection.get(y),1);
                                                    coupCap.add(pt);
                                                    pt=new Coup(PionsEnAction.get(x).posX,PionsEnAction.get(x).posY,p,listdirection.get(y),0);
                                                    coupCap.add(pt);
                                            }
                                    }else{
                                            pt=new Coup(PionsEnAction.get(x).posX,PionsEnAction.get(x).posY,p,listdirection.get(y),0);
                                            coupNCap.add(pt);
                                    }
				}
		 }
	}

	public ArrayList<Coup> Bestcoup(Jeu j){
			jeu=(Jeu)j.clone();
			ArrayList<Coup> CoupSolution =null;
			boolean evalPlateau = true;
        
			// ALPHA BETA
			listeSol = new ArrayList<Coup>();
			CoupSolution = this.alphaBeta(profondeur,evalPlateau);  // simule x-profondeur tours
												  				 															
			return CoupSolution;
	}

	public ArrayList<Coup> alphaBeta(int profondeur, boolean evalPlateau){
		ArrayList<Coup> Resultat = new ArrayList<Coup>();
		ArrayList<Coup> ResultatCourant = new ArrayList<Coup>();
		ArrayList<Coup> coupSolution = null;

		ArrayList<Coup> listeCoup=new ArrayList<Coup>();
		Random r= new Random();

		Coup coupCourant=null;

		int valMax = MIN, valTemp, nbPionsManges;
		Integer alpha = new Integer(MIN);
		Integer beta = new Integer(MAX);
		this.RemplirList(jeu.plateau);

		if(coupCap.size() > 0){
			 listeCoup = (ArrayList<Coup>)coupCap.clone();
		}else if(coupNCap.size() > 0){
			 listeCoup = (ArrayList<Coup>) coupNCap.clone();
		}
		int i=0;
		int longeur=listeCoup.size();
		int x;
		while(i<longeur){

			casesParcourue.clear();
			ResultatCourant.clear();
			coupCourant =(Coup)listeCoup.get(i).clone();
			Coup  c = (Coup)coupCourant.clone();
			x=coupCourant.posX;
			coupCourant.posX=coupCourant.posY;
			coupCourant.posY=x;
			Ldirection = coupCourant.direction;
			casesParcourue.add(new Couple(coupCourant.posX,coupCourant.posY));
			nbPionsManges = coupCourant.execute() * this.coeffPionsManges;
			ResultatCourant.add((Coup)coupCourant.clone());

			valTemp = nbPionsManges;
			Plateau newP=(Plateau)coupCourant.p.clone();


			//TRAITEMENT..............
            
			ArrayList<Coup> listetemp = new ArrayList<Coup>();
			listetemp.add(coupCourant);
			Noeud n = jeu.miseAjourPion(coupCourant.posX,coupCourant.posY,coupCourant.direction);
			RemplirListPionABouger(newP,n.posX,n.posY);
			ArrayList<Coup> malist = (ArrayList<Coup>)coupCap.clone();
			TRAITEMENT(listetemp,nbPionsManges,newP, malist);
			ArrayList<Coup> listeSolLocal=(ArrayList<Coup> )listeSol.clone();

			valTemp = nbPionsM;
			nbPionsM = 0;
			valTemp += min(profondeur-1, alpha, beta,newP, evalPlateau);
			if(valTemp > valMax){
				valMax = valTemp;

				coupSolution = (ArrayList<Coup> )listeSolLocal.clone();

			}else if(valTemp == valMax){ // Choix randomise si des solutions ont un resultat similaire
				if(r.nextInt(2) == 1){
					valMax = valTemp;
					coupSolution = (ArrayList<Coup> )listeSolLocal.clone();
				}
			}
			i++;
		}
		return coupSolution;
	}



	public int min(int profondeur, Integer alpha, Integer beta, Plateau terrainCourant, boolean evalPlateau){
		//Cas de base
		if(profondeur == 0){
			if(evalPlateau)
				return evalPlateau(terrainCourant);
			else
				return 0;
		}
		
		ArrayList<Coup> Resultat = new ArrayList<Coup>();
		ArrayList<Coup> ResultatCourant = new ArrayList<Coup>();
		ArrayList<Coup> coupSolution = null;

		ArrayList<Coup> listeCoup=new ArrayList<Coup>();
		Random r= new Random();
		Coup coupCourantMin;
		Coup coupCourant=null;
		int valTemp = MAX, valRes = MAX,nbPionsManges;
		if(this.valPion==2)this.valPion=1;
                else this.valPion=2;
		this.RemplirList(terrainCourant);
		if(this.valPion==2)this.valPion=1;
                else this.valPion=2;
                
		int list=0;
		if(coupCap.size() > 0){
			 listeCoup = (ArrayList<Coup>)coupCap.clone();
		}else if(coupNCap.size() > 0){
			 listeCoup = (ArrayList<Coup>) coupNCap.clone();
		}
		if(list == 1 && coupNCap.isEmpty()) return MAX;

		int i=0;
		int longeur=listeCoup.size();
		while(i<longeur){
			coupCourantMin =(Coup) listeCoup.get(i).clone();
			int x=coupCourantMin.posX;
			coupCourantMin.posX=coupCourantMin.posY;
			coupCourantMin.posY=x;
			i++;

			nbPionsManges = coupCourantMin.execute() * this.coeffPionsManges;
			valTemp = -nbPionsManges; // nombre de pions perdus (manges par l'adversaire) en negatif
			Plateau newP=(Plateau)coupCourantMin.p.clone();

			//TRAITEMENT..............
			ArrayList<Coup> listetemp = new ArrayList<Coup>();
			listetemp.add(coupCourantMin);
			Noeud n = jeu.miseAjourPion(coupCourantMin.posX,coupCourantMin.posY,coupCourantMin.direction);
			RemplirListPionABouger(newP,n.posX,n.posY);
			ArrayList<Coup> malist = (ArrayList<Coup>)coupCap.clone();
			TRAITEMENT(listetemp,nbPionsManges,newP, malist);
			valTemp = -nbPionsM;
			nbPionsM = 0;


			valTemp += max(profondeur-1, alpha, beta, newP, evalPlateau);
			valRes = Math.min(valTemp, valRes);
			if(alpha >= valTemp) // elagage
				return valTemp;
			beta = Math.min(beta,valRes);
		}
		return valRes;
	}


		public int max(int profondeur, Integer alpha, Integer beta,  Plateau terrainCourant, boolean evalPlateau){
			//Cas de base
			if(profondeur == 0){
				if(evalPlateau)
					return evalPlateau(terrainCourant) ;
				else
					return 0;
			}
			
			ArrayList<Coup> Resultat = new ArrayList<Coup>();
			ArrayList<Coup> ResultatCourant = new ArrayList<Coup>();
			ArrayList<Coup> coupSolution = null;

			ArrayList<Coup> listeCoup=new ArrayList<Coup>();
			Random r= new Random();
			Coup coupCourantMax=null;
			int valTemp = MAX, valRes = MAX,nbPionsManges;
			//this.valPion=2;
			this.RemplirList(terrainCourant);

			int list=0;
			if(coupCap.size() > 0){
				 listeCoup = (ArrayList<Coup>)coupCap.clone();
			}else if(coupNCap.size() > 0){
				 listeCoup = (ArrayList<Coup>) coupNCap.clone();
			}
			if(list == 1 && coupNCap.isEmpty()) return MAX;

			int i=0;
			int longeur=listeCoup.size();
			while(i<longeur){
				coupCourantMax = (Coup) listeCoup.get(i).clone();
				int x=coupCourantMax.posX;
				coupCourantMax.posX=coupCourantMax.posY;
				coupCourantMax.posY=x;
				i++;

				valTemp = coupCourantMax.execute() * this.coeffPionsManges;
				Plateau newP=(Plateau)coupCourantMax.p.clone();
				//TRAITEMENT..............
				ArrayList<Coup> listetemp = new ArrayList<Coup>();
				listetemp.add(coupCourantMax);
				Noeud n = jeu.miseAjourPion(coupCourantMax.posX,coupCourantMax.posY,coupCourantMax.direction);
				RemplirListPionABouger(newP,n.posX,n.posY);
				ArrayList<Coup> malist = (ArrayList<Coup>)coupCap.clone();
				TRAITEMENT(listetemp,valTemp,newP, malist);
				valTemp = nbPionsM;
				nbPionsM = 0;

				valTemp += min(profondeur-1, alpha, beta, newP, evalPlateau);
				valRes = Math.max(valTemp, valRes);
				if(valTemp >= beta)
					return valTemp;
				alpha = Math.max(alpha,valRes);
			}
			return valRes;
		}


		public int jouerCoup(Coup c){
			int v;
			Random r= new Random();
			c.p.setSucc(c.posX,c.posY);
			switch(c.type){
                            case 1:
                                v= c.p.DeplacePionApproche(c.posX,c.posY,c.direction);
                                break;
                            case 0:
                                v= c.p.DeplacePionEloigner(c.posX,c.posY,c.direction);
                                break;
                            default:
                                v=0;
			}
			return v;
		}

		public int evalPlateau(Plateau terrainCourant){
			int resultat = 0;

			for(int ligne = 0; ligne <= 4; ligne++)
				for(int colonne = 0; colonne <= 8; colonne++)
					if(terrainCourant.grille[ligne][colonne].value ==2)
						resultat +=1;
					else if (terrainCourant.grille[ligne][colonne].value ==1)
						resultat -=1;
			return resultat;
		}

		public void RemplirListPionABouger(Plateau p,int posX,int posY){
			this.PionsEnAction.clear();
			this.PionsEnActionDeplacement.clear();
			this.directionP.clear();
			this.coupCap.clear();
		  this.coupNCap.clear();
			int type;
			Coup pt;
			p.CLEARSucc();
			for (int i = 0; i < p.LIGNE; i++) {
			   for (int j = 0; j < p.COLONNE; j++) {
					 p.setSucc(i,j);
					 	if(p.grille[i][j].value==this.valPion){
								if(p.grille[i][j].successeurs.size()!=0){
										PionsEnAction.add(p.grille[i][j]);
										PionsEnActionDeplacement.add(p.grille[i][j].successeurs);

								}
						}
				 }
			 }
			 for (int x = 0; x < PionsEnAction.size(); x++) {
					ArrayList<Integer> listdirection = PionsEnActionDeplacement.get(x);
					for (int y = 0; y < listdirection.size(); y++) {
						if(PionsEnAction.get(x).posX==posX && PionsEnAction.get(x).posY==posY){
							 if(p.DeplacePionAvecCapture(PionsEnAction.get(x).posX,PionsEnAction.get(x).posY,listdirection.get(y))){
								 type = p.typeDeplacement(PionsEnAction.get(x).posX, PionsEnAction.get(x).posY,listdirection.get(y));
								 if(type == 0){
									 pt=new Coup(PionsEnAction.get(x).posX,PionsEnAction.get(x).posY,p,listdirection.get(y),0);
									 coupCap.add(pt);
								 }else if(type == 1){
									 pt=new Coup(PionsEnAction.get(x).posX,PionsEnAction.get(x).posY,p,listdirection.get(y),1);
									 coupCap.add(pt);
								 }else if(type == 2){
									 pt=new Coup(PionsEnAction.get(x).posX,PionsEnAction.get(x).posY,p,listdirection.get(y),1);
									 coupCap.add(pt);
									 pt=new Coup(PionsEnAction.get(x).posX,PionsEnAction.get(x).posY,p,listdirection.get(y),0);
									 coupCap.add(pt);
								 }
							 }
					 }
				 }
			 }
		}
		//TRAITEMENT..............
		public void TRAITEMENT(ArrayList<Coup> listetemp,int val,Plateau newP, ArrayList<Coup> listeSecondCoup){

							if(val<=0){
									listeSol=listetemp;
							}else if(listeSecondCoup.size()==0){
											if(val > Max){
												listeSol=listetemp;
												nbPionsM=val;
												int lP = casesParcourue.size();
											}
							}else{
									int j=0;
									int longeurll=listeSecondCoup.size();
									int nbPionsManges=0;
									while(j < longeurll){
											//listetemp
											ArrayList<Coup> l = (ArrayList<Coup>)listetemp.clone();
											Coup ss = (Coup)listeSecondCoup.get(j);
											Noeud Nparcourue = jeu.miseAjourPion(ss.posX,ss.posY,ss.direction);
											int lP = casesParcourue.size();
											boolean bool = true;
											for(int i=0;i<lP;i++){
													Couple cp = casesParcourue.get(i);
													if(cp.x==Nparcourue.posX &&cp.y==Nparcourue.posY ){
														bool = false;
													}
											}
											if(listetemp.get(listetemp.size()-1).direction == ss.direction) bool = false;
											if(bool){
												int i=0;


												ss.p = (Plateau)newP.clone();
												l.add(ss);
												Coup coupCourant=(Coup)listeSecondCoup.get(j);
												//val
												nbPionsManges = coupCourant.execute() * this.coeffPionsManges;
												//newPlateau
												Plateau newPlateau=(Plateau)coupCourant.p.clone();
												//lSecond
												Noeud n=jeu.miseAjourPion(coupCourant.posX,coupCourant.posY,coupCourant.direction);

												casesParcourue.add(new Couple(coupCourant.posX,coupCourant.posY));
												RemplirListPionABouger(newPlateau,n.posX,n.posY);

												casesParcourue.remove(new Couple(coupCourant.posX,coupCourant.posY));
												ArrayList<Coup> lSecond=(ArrayList<Coup>)coupCap.clone();

												//TRAITEMENT..............
												TRAITEMENT(l,val+nbPionsManges,newPlateau,lSecond);
											}
											j++;

									}
							}

		}

                
                public void setProfondeur(int i){
                    this.profondeur = i;
                }
}

