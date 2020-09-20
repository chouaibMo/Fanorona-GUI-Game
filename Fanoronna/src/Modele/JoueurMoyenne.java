package Modele;
import Util.*;

import	java.util.*;
import java.io.Serializable;
import java.util.ArrayList;


public class JoueurMoyenne extends Joueur implements Serializable{

	public final int MIN = -1000;
	public  ArrayList<Noeud> PionsEnAction;			//Contient tous Les Pions qui peuvent se deplacer dans le plateau courant
	public  ArrayList<ArrayList<Integer>> PionsEnActionDeplacement;
	public  ArrayList<Coup> coupCap;						//Contient les triples (posX,posY,direction) ou il y'a au moins une capture
	public  ArrayList<Coup> coupNCap;						//Contient les triples (posX,posY,direction) ou il y'a pas de captures
	public  Jeu jeu;														//Le jeu


	ArrayList<Coup> listeSol;										//liste des capture (des coups) La meillieur!
	int nbP=0; 																	//nombre de pions Manges dans une capture
	public  ArrayList<Couple> casesParcourue;   //cases deja Parcourue dans un coup
	public  int Ldirection;											//Derniere direction suivi dans le dernier coup



	/*  CONSTRUCTEURS:
	 *  Cree un Joueur Glutton avec valPion i.
	 */
	public JoueurMoyenne(int i){
		this.name = "IA Moyenne";
		this.value = 0;
		this.nbPions = 22;
		this.valPion = i;

		this.PionsEnAction= new ArrayList<Noeud>();
		this.PionsEnActionDeplacement= new ArrayList<ArrayList<Integer>>();
		this.coupCap=new ArrayList<Coup>();
		this.coupNCap=new ArrayList<Coup>();
		this.casesParcourue=new ArrayList<Couple>();
	}

	//Rempli les 2 listes coupCap et coupNCap
	public void RemplirList(Plateau p){
		this.PionsEnAction.clear();
		this.PionsEnActionDeplacement.clear();
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
			listeSol = new ArrayList<Coup>();

			CoupSolution = this.alphaBeta();

			return CoupSolution;
	}

	public ArrayList<Coup> alphaBeta(){
		ArrayList<Coup> coupSolution = null;

		ArrayList<Coup> listeCoup=new ArrayList<Coup>();
		Random r= new Random();

		Coup coupCourant=null;

		int valMax = MIN, valTemp, nbPionsManges;
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
			coupCourant =(Coup)listeCoup.get(i).clone();
			x=coupCourant.posX;
			coupCourant.posX=coupCourant.posY;
			coupCourant.posY=x;

			Ldirection = coupCourant.direction;
			casesParcourue.add(new Couple(coupCourant.posX,coupCourant.posY));
			nbPionsManges = coupCourant.execute() ;

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

			valTemp = nbP;
			nbP = 0;
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

		public int jouerCoup(Coup c){
			int i,l,d,type;
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
							v=-1;
			}
			return v;
		}

		public void RemplirListPionABouger(Plateau p,int posX,int posY){
			this.PionsEnAction.clear();
			this.PionsEnActionDeplacement.clear();
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
									nbP=val;
							}else if(listeSecondCoup.size()==0){
											if(val >MIN){
												listeSol=listetemp;
												nbP=val;
											}
							}else{
									int j=0;
									int longeurll=listeSecondCoup.size();
									int nbPionsManges=0;
									while(j < longeurll){

											Coup coupCourant=(Coup)listeSecondCoup.get(j);
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
											if(Ldirection==coupCourant.direction) bool = false;
											if(listetemp.get(listetemp.size()-1).direction == ss.direction) bool = false;
											if(bool){
												int i=0;


												ss.p = (Plateau)newP.clone();
												l.add(ss);
												//val
												nbPionsManges = coupCourant.execute() ;
												//newPlateau
												Ldirection = coupCourant.direction;
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
}
