package Modele;
import java.io.Serializable;
import java.util.*;

public class Noeud implements Serializable{
	public static final int VIDE   = 0;
	public static final int P1     = 1;
	public static final int P2     = 2;

	public  int value;
	public  int posX;
	public  int posY;
	public  ArrayList<Integer> Voisins = new ArrayList<Integer>();
	public  ArrayList<Integer> successeurs = new ArrayList<Integer>();

	/*Constructeur : cree un case de valeur v = 0,1 ou 2. Et de position(x,y)*/
	Noeud(int v,int x,int y){
		this.value = v;
		this.posX = x;
		this.posY = y;
	}

	public Noeud clone(){
		Noeud copie = new Noeud(this.value,this.posX,this.posY);
		copie.Voisins =(ArrayList<Integer>)this.Voisins.clone();
		copie.successeurs =(ArrayList<Integer>)this.successeurs.clone();
		return copie;
	}
	/*Renvoie la valeur de noeud*/
	public int getValue() {
		return value;
	}

	/*mets la valeur de noeud v*/
	public void setValue(int v) {
		this.value = v;
	}


	/*Renvoie vraie si la valeur de noeud est 0(ie: Noeud VIDE)*/
	public boolean estVide() {
		return value==VIDE;
	}

	/*Renvoie vraie si la valeur de noeud est 1(ie: Noeud P1)*/
	public boolean estP1() {
		return value==P1;
	}

	/*Renvoie vraie si la valeur de noeud est 2(ie: Noeud P2)*/
	public boolean estP2() {
		return value==P2;
	}

	public boolean estVoisin(int d){
	 		return Voisins.contains(d);
	}


	public int Direction(Noeud Na){
      if((this.posX-1==Na.posX)&&(this.posY==Na.posY)) return Direction.N;
			if((this.posX+1==Na.posX)&&(this.posY==Na.posY)) return Direction.S;
			if((this.posX==Na.posX)&&(this.posY+1==Na.posY)) return Direction.E;
			if((this.posX==Na.posX)&&(this.posY-1==Na.posY)) return Direction.O;

			if((this.posX-1==Na.posX)&&(this.posY-1==Na.posY)) return Direction.NO;
			if((this.posX+1==Na.posX)&&(this.posY+1==Na.posY)) return Direction.SE;

			if((this.posX-1==Na.posX)&&(this.posY+1==Na.posY)) return Direction.NE;
			if((this.posX+1==Na.posX)&&(this.posY-1==Na.posY)) return Direction.SO;
			return -8;
  }
        
        public Noeud noeudDirec(int i){
                switch(i){
                    case 0:
                        return new Noeud(0,this.posX-1,this.posY);
                        
                    case 1:
                    return new Noeud(0,this.posX-1,this.posY+1);
                       
                    case 2:
                    return new Noeud(0,this.posX,this.posY+1);
                     
                    case 3:
                    return new Noeud(0,this.posX+1,this.posY+1);
                       
                    case 4:
                    return new Noeud(0,this.posX+1,this.posY);
                        
                    case 5:
                    return new Noeud(0,this.posX+1,this.posY-1);
                        
                    case 6:
                    return new Noeud(0,this.posX,this.posY-1);
                       
                    case 7:
                    return new Noeud(0,this.posX-1,this.posY-1);
                        
                }
                return null;
        }
}
