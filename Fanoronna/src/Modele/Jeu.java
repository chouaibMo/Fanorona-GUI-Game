package Modele;
import Util.*;
import java.util.*;
import java.io.Serializable;

public class Jeu implements Serializable{
    public String winner;
	public ConsoleColors cc;  // gere le changement de couleurs sur le terminal
	public Mode modeJeu; 	// Vaut 0 si (Joueur VS Joueur) Sinon le 1,2,3 selon l'IA
	public Plateau plateau;		// Plateau de jeu
	public Joueur jCourant,player1, player2;	// Les Joueurs
	public int LastDirection = -1;		// La derniere direction que jCourant a suivi
	public Noeud PionABouger = null;	// Le pion deja bouger par jCourant dans un coup
	public  ArrayList<Couple> CaseParcourue = new ArrayList<Couple>();	// liste des cases Parcourue dans un coup
	
    public Jeu(Joueur j1,Joueur j2){
		cc = new ConsoleColors();
		this.plateau = new Plateau();
		this.modeJeu = new Mode(0);
		this.player1 = j1;
		this.player2 = j2;
		this.jCourant = this.player1;
		initCaseP();
	}

	public Jeu clone(){
		Jeu copie = new Jeu(this.player1,this.player2);
		copie.plateau = (Plateau)this.plateau.clone();
		copie.modeJeu = (Mode)this.modeJeu.clone() ;
		copie.jCourant = (Joueur)this.jCourant.clone();
		copie.player1 = (Joueur)this.player1.clone();
		copie.player2 = (Joueur)this.player2.clone();
		copie.LastDirection = this.LastDirection;
		if(this.PionABouger!=null)copie.PionABouger = (Noeud)this.PionABouger.clone();
                copie.CaseParcourue = (ArrayList<Couple>)this.CaseParcourue.clone();
		return copie;
	}

	public Jeu(Joueur j1,Joueur j2,Plateau p,Mode m,Joueur jc){
		cc = new ConsoleColors();
		this.plateau = p;
		this.modeJeu = m ;
		this.player1 = j1;
		this.player2 = j2;
		this.jCourant = jc;
		initCaseP();
	}

	public void initJeu(){
		this.plateau = new Plateau();
		this.modeJeu = new Mode(0);
		this.jCourant = this.player1;
		this.PionABouger = null;
		this.LastDirection = -1;
		initCaseP();
	}

	/*Affiche la liste des cases deja Parcourue*/
        
        public boolean contient(Noeud n){
            for (int i = 0; i < CaseParcourue.size(); i++) {
                if(CaseParcourue.get(i).x==n.posX && CaseParcourue.get(i).y==n.posY) return true; 
            }
            return false;
        }

	/*Initialise la liste des cases Parcourue */
	public void initCaseP(){
			CaseParcourue.clear();
	}


	/* mets m comme mode*/
	public void setMode(int m){
		modeJeu.setMode(m);
	}

	/* mets j comme jCourant*/
	public void setJCourant(Joueur j){
		this.jCourant = j;
	}

	/* Renvoie le le plateau de jeu*/
	public Plateau getPlateau(){
		return plateau;
	}

	/* Renvoie le 1 er joueur*/
	public Joueur getPlayer1(){
		return player1;
	}

	/* Renvoie le 2 eme joueur*/
	public Joueur getPlayer2(){
		return player2;
	}

	public void setPlayer1(Joueur j){
		this.player1 = j;
	}

	public void setPlayer2(Joueur j){
		this.player2 = j;
	}

	/* Renvoie le mode de Jeu */
	public Mode getModeJeu(){
		return modeJeu;
	}

  /* vrai si la partie est finie */
	public boolean finJeu(){
            int x=0,y= 0;
            for (int i = 0; i < plateau.LIGNE; i++) {
                for (int j = 0; j < plateau.COLONNE; j++) {
                    if(plateau.grille[i][j].value == 1){
                        x=1;
                    }
                    if(plateau.grille[i][j].value == 2){
                        y=1;
                    }
                    if(x==1 & y==1){
                        return false;
                    }
                }
            }
            if(x==1 & y!=1){
                winner= player1.name;
            }else{
                winner= player2.name;
            }
            return true;
	}


	/*public void remplirHistorique(int l, int c,Plateau p,int d, int cp){
		this.historique.faire(new Coup(l,c,p,d,cp));
	}*/

  /*  Renvoi le nombre de pions manges apres un coup, -1 si deplacement illegal
	 *  l,c : coordonnes d'un pion,  d : sens de deplacement,  cp : approcher(1)/eloigner(0)
	 */
	public int coup(int l, int c,int d, int cp){
		Coup[] tabCoup = new Coup[5];
		Coup coup = new Coup(l,c,this.plateau,d,cp);
		int n = jCourant.jouerCoup(coup);
		if(jCourant.equals(player1) && n!=-1){
				player2.nbPions = player2.nbPions-n;
				LastDirection = d;
		}else if(jCourant.equals(player2) && n!=-1){
				player1.nbPions = player1.nbPions-n;
				LastDirection = d;
		}
		return n;
	}

	/*	Renvoie vrai c'est le deplacement est correcte. */
	public boolean depValid(int l, int c,int d,int type){
			plateau.setSucc(l,c);

			if(plateau.grille[l][c].getValue()!=jCourant.valPion){
				System.out.println(" (plateau.grille[l][c]"+plateau.grille[l][c]);
				System.out.println(" (plateau.grille[l][c]!=jCourant.valPion)");
				return false;
			}

			if(!(PionABouger==null)&& !(PionABouger.posX==l&& PionABouger.posY==c)/*!(PionABouger == plateau.grille[l][c]))*/){
				System.out.println(" PionABouger"+PionABouger.posX+"   y"+PionABouger.posY);
				return false;
			}

			if((PionABouger!=null) && !plateau.DeplacePionAvecCapture(l,c,d)){
				System.out.println(" Ne peut pas jouer, il faut valider!");
				return false;
			}

			if(LastDirection == d){
				System.out.println("LastDirection == d");
				return false;
			}

			if(!plateau.grille[l][c].successeurs.contains(d)){
				System.out.println(" !plateau.grille[l][c].successeurs.contains(d)");
				return false;
			}

			if(plateau.PeutManger(jCourant) && !plateau.DeplacePionAvecCapture(l,c,d)){
				System.out.println(" !DeplacePionAvecCapture(l,c,d)");
				return false;
			}

			for (int i = 0; i < CaseParcourue.size(); i++) {
				if(miseAjourPion(l,c,d).posX==CaseParcourue.get(i).x &&miseAjourPion(l,c,d).posY==CaseParcourue.get(i).y){
					System.out.println(" case deja Parcourue ");
					return false;
				}
			}
 			return true;
	}


	/*Pour mettre a jour PionABouger afin que le joueur ne puisse pas deplacer un autre pion*/
	public Noeud miseAjourPion(int l,int c,int d){
		switch(d) {
			case 0:
				// "NORD";
				return plateau.grille[l-1][c];
			case 1:
				// "NORD EST";
				return plateau.grille[l-1][c+1];
			case 2:
				// "EST";
				return plateau.grille[l][c+1];
			case 3:
				// "SUD EST";
				return plateau.grille[l+1][c+1];
			case 4:
				// "SUD";
				return plateau.grille[l+1][c];
			case 5:
				// "SUD OUEST";
				return plateau.grille[l+1][c-1];
			case 6:
				// "OUEST";
				return plateau.grille[l][c-1];
			default:
				// "NORD OUEST";
				return plateau.grille[l-1][c-1];
		}
	}

	/* Changer le joueur courant */
	public void switchPlayer(){
		if(jCourant.equals(player1))
				setJCourant(player2);
		else
			 setJCourant(player1);
	}

/*--------------------------------------Version sur terminal------------------------------------------*/
		/*
		 *  redemande a l'utilisateur de saisir tant que la saisie est invalide
		 *  deplacement illegal => nb pions manges=-1
		 */
			public int jouer(){
				Scanner sc = new Scanner(System.in);
				int l,c,d,p;
				int nb = -1;
				Sauvegarde<Jeu> s = new Sauvegarde();

				String jcourant = cc.col("["+jCourant.getName()+"]",cc.YELLOW_B); //changer la couleur en Jaune Gras
				System.out.print(jcourant+" : veuillez saisir un noeud(x,y), direction(d) et mouvement(1/0) :");
				l = sc.nextInt();

				if(l==9) return 0;
				if (l==10) 	{
					s.save("Sauvegarde",(Jeu)this.clone());
					System.exit(0);
				}
				c = sc.nextInt();
				d = sc.nextInt();
				p = sc.nextInt();
				if(depValid(l,c,d,p)&& (nb=coup(l,c,d,p))>=0){
		        PionABouger = miseAjourPion(l,c,d);
		        CaseParcourue.add(new Couple(l,c));

		    }
				return nb;
			}


		public void partie(){
			int nb;      // coups du debut de la partie
			while(!finJeu()){
					do{
	            do{//meme joueur rejoue tant que nb pions manges >0
								infos();
								nb = jouer();
							}while(nb==-1);
							//mettreajour pions
					}while(nb>0);
					switchPlayer();     //changer le joueur courant
					LastDirection = -1;
					PionABouger = null;
					initCaseP();
			}
			System.out.println("PARTIE TERMINEE - "+jCourant.getName()+" A GAGNE");
		}

		/*Affiche sur le terminal le nombre de pions restants pour chaque joueur */
		public void score(){
			String local = cc.col("LOCAL  :"+player1.nbPions, cc.RED_B);        //Changer la couleur de la chaine en Rouge Gras
			String visit = cc.col("VISITOR  :"+player2.nbPions, cc.GREEN_B);    //Changer la couleur de la chaine en Vert Gras
			System.out.println(" +-------------------+---------------------+");
			System.out.println(" |    "+local+"     |    "+visit+"     |");
			System.out.println(" +-------------------+---------------------+");
			System.out.println("");
		}

/*  Afficher les informations de la partie
 *  afficherDirection : tableau des differentes directions
 *  grille.afficher   : plateau du jeu
 *  score             : nb de pions restants pour chaque joueur
 */
	public void infos(){
		Direction.afficherDirection();
		plateau.afficher();
		score();
	}
}
