package Util;
import java.io.Serializable;


public class triple implements Serializable{
	public int x;
	public int y;
	public int d;


/*  CONSTRUCTEURS:
 *  Cree un triple d'entiers (x,y,z).
 */
	public triple(int i,int j,int direction){
		x=i;
		y=j;
		d=direction;
	}
}
