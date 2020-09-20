package thread;

import Modele.Jeu;

public class MyThread{

    public static void sleep(long millis,Jeu j){
        try{
            if(j.jCourant==j.player2) Thread.sleep(millis);
        }
        catch (InterruptedException e){
           throw new RuntimeException(e);
        }
    }

    public static void sleep(long millis){
        try{
                Thread.sleep(millis);
        }catch (InterruptedException e){
           throw new RuntimeException(e);
        }
    }
}
