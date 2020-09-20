package Modele;
import java.io.Serializable;

public class Coup implements Serializable{

  public int posX;
  public int posY;
  public Plateau p;
  public int direction;
  public int type;

  /*  CONSTRUCTEURS:
   *  Cree un coup avec une case de depart (posX,posY) de direction (direction) et de type (type) dans le plateau p.
   */
    
  public Coup(int posX, int posY, Plateau p,int d, int cp){
    this.posX = posX;
    this.posY = posY;
    this.p = p;
    this.direction = d;
    this.type = cp;
  }

   public int execute(){
    if(type == 0)
        return p.DeplacePionEloigner(posX, posY, direction);
    else
        return p.DeplacePionApproche(posX, posY, direction);
    }

    public Coup clone(){
  		Coup copie = new Coup(this.posY,this.posX,this.p.clone(),this.direction,this.type);
  		return copie;
  	}

  public void desexecute(){

    if(p.DeplacePionAvecCapture(posX, posY, direction)){
      if(type == 0){
        p.DeplacePionEloigner(posX, posY, direction);
      }else {
        p.DeplacePionApproche(posX, posY, direction);
      }
    }
    if(p.DeplacePionAvecCapture(posY, posX, direction))
        if (type == 0) {
          p.DeplacePionEloigner(posY, posX, direction);
        }else {
          p.DeplacePionApproche(posY, posX, direction);
        }
  }

}
