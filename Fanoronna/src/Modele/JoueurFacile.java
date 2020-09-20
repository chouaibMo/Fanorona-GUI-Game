package Modele;
import Modele.*;
import Util.*;

import	java.util.*;
import java.io.Serializable;

public class JoueurFacile extends Joueur implements Serializable{

	public  ArrayList<Noeud> PionsEnAction= new ArrayList<Noeud>();// Contient les noueds qui peuvent se deplacer en capture ou sans
	public  ArrayList<ArrayList<Integer>> PionsEnActionDeplacement= new ArrayList<ArrayList<Integer>>(); //Contient une liste d'entiers (direction possibles)
	public  ArrayList<Integer> directionP= new ArrayList<Integer>();// liste d'entiers (directions)
	public  ArrayList<Coup> coupCap=new ArrayList<Coup>();//Contient les triples (posX,posY,direction) ou il y'a au moins une capture
	public  ArrayList<Coup> coupNCap=new ArrayList<Coup>();//Contient les triples (posX,posY,direction) ou il y'a pas de captures
	public Jeu jeu;

	/*  CONSTRUCTEURS:
	 *  Cree un Joueur avec name s et valPion i.
	 */
	public JoueurFacile(int i){
		this.name = "IA Facile";
		this.value = 0;
		this.nbPions = 22;
		this.valPion = i;
	}

	public void RemplirList(Plateau p){
		coupCap.clear();
	  coupNCap.clear();
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

	public int jouerCoup(Coup c){
		int i,l,d,type;
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

	public int secondMove(){
            int v=0;
            int i,l,type,d;
            Random r= new Random();
            ArrayList<Integer> listdirection = new ArrayList<Integer>();
            jeu.plateau.setSucc(jeu.PionABouger.posX,jeu.PionABouger.posY);
            listdirection.clear();
            for (int y = 0; y < jeu.PionABouger.successeurs.size(); y++) {
                if(jeu.plateau.DeplacePionAvecCapture(jeu.PionABouger.posX,jeu.PionABouger.posY,jeu.PionABouger.successeurs.get(y))) listdirection.add(jeu.PionABouger.successeurs.get(y));
            }
            if(listdirection.size()!=0){
                i=r.nextInt(listdirection.size());
                l=jeu.PionABouger.posX;
                int y=jeu.PionABouger.posY;
                d=listdirection.get(i);
                type = jeu.plateau.typeDeplacement(l,y,d);
                if(type==2) type = r.nextInt(2);
                Coup c=new Coup(l,y, jeu.plateau,d,type);
                if(jeu.depValid(l,y,d,type)){
                        if(type==1) v=c.p.DeplacePionApproche(l,y,d);
                        else  v=c.p.DeplacePionEloigner(l,y,d);

                        jeu.PionABouger = jeu.miseAjourPion(l,y,d);
                        jeu.LastDirection=d;
                        jeu.CaseParcourue.add(new Couple(l,y));
                        }
        }
                    return v;

	}

	public Coup joue(Jeu jeu) {
            Random r= new Random();
            this.jeu=jeu;
            int l=0,c=0,d=0;
            int type=0;int nb=-1;
            RemplirList(jeu.plateau);
            int i;
            if(coupCap.size()!=0){
                    i=r.nextInt(coupCap.size());
                    l=coupCap.get(i).posX;
                    c=coupCap.get(i).posY;
                    d=coupCap.get(i).direction;
                    type = coupCap.get(i).type;
            }else if(coupNCap.size()!=0){
                    i=r.nextInt(coupNCap.size());
                    l=coupNCap.get(i).posX;
                    c=coupNCap.get(i).posY;
                    d=coupNCap.get(i).direction;
                    type = coupNCap.get(i).type;
            }
            if(jeu.depValid(l,c,d,type)){
                            Coup coup;
                            jeu.PionABouger = jeu.miseAjourPion(l,c,d);
                            jeu.LastDirection=d;
                            jeu.CaseParcourue.add(new Couple(l,c));
                            PionsEnAction.clear();
                            PionsEnActionDeplacement.clear();
                            directionP.clear();
                            coupCap.clear();
                            coupNCap.clear();
                            coup = new Coup(l,c,jeu.plateau,d,type);
                            return coup;
            }
            return null;
		}
}
