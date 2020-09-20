package Modele;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Sauvegarde<E> {

  public String save(String filename, E obj){
    try {
            FileOutputStream fos = new FileOutputStream("src/Sauvegarde/"+filename);
  	    ObjectOutputStream oos = new ObjectOutputStream(fos);
  	    oos.writeObject(obj);
        System.out.println("La partie a ete sauvegardee avec succes");
        return "La partie a ete sauvegardee avec succes";
  	} catch (Exception e) {
  	    System.out.println("Saving error ...");
            return "echec de la sauvegarde";

  	}
      }

  public E load(String filename) {
    try {
        File f = new File("src/Sauvegarde/"+filename);
        FileInputStream   fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);
        E obj = (E)(ois.readObject());
        System.out.println("load done");
        return obj;
	} catch (Exception e) {
  	  System.out.println("Loading error ...");
  	  return null;
  	}
  }

}
