package Modele;

import java.io.Serializable;

public class Direction implements Serializable {
  
  public static final int N  = 0;
  public static final int NE = 1;
  public static final int E  = 2;
  public static final int SE = 3;
  public static final int S  = 4;
  public static final int SO = 5;
  public static final int O  = 6;
  public static final int NO = 7;

  /* affiche un tableau qui montre a l'utilisateur les valeurs da chaque direction */
    
  public static void afficherDirection(){
    System.out.println(" +-----------+---+----+---+----+---+----+---+----+");
    System.out.println(" | Direction | N | NE | E | SE | S | SO | O | NO |");
    System.out.println(" +-----------+---+----+---+----+---+----+---+----+");
    System.out.println(" |   Valeur  | 0 |  1 | 2 |  3 | 4 |  5 | 6 |  7 |");
    System.out.println(" +-----------+---+----+---+----+---+----+---+----+");
    System.out.println("");
  }

  /* Renvoie un String de la direction qui correspond a l'entier i*/
    
  public static String toString(int i){
    String s ="";
    switch(i) {
      case 0:
        s = "NORD";
        break;
      case 1:
        s= "NORD EST";
        break;
      case 2:
        s= "EST";
        break;
      case 3:
        s= "SUD EST";
        break;
      case 4:
        s= "SUD";
        break;
      case 5:
        s= "SUD OUEST";
        break;
      case 6:
        s= "OUEST";
        break;
      default:
        s= "NORD OUEST";
    }
    return s;
  }

}
