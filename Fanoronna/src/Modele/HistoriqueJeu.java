package Modele;

import java.util.*;
import java.io.Serializable;

public class HistoriqueJeu implements Serializable {
    
  //deux pile passe et futur pour empiler les Jeux
    
  public Deque<Jeu> passe;
  public Deque<Jeu> futur;

  public HistoriqueJeu() {
		this.passe = new ArrayDeque<Jeu>();
		this.futur = new ArrayDeque<Jeu>();
	}
  

	public boolean peutAnnuler() {
		return !passe.isEmpty();
	}

	public boolean peutRefaire() {
		return !futur.isEmpty();
	}

	public Jeu transfert(Deque<Jeu> source, Deque<Jeu> destination) {
		Jeu resultat = (Jeu)source.removeFirst().clone();
		destination.addFirst(resultat);
		return resultat;
	}

  /*
  *Annule un Jeu , on depile le Jeu dans passe , on le desexecute
  * et on l'empile dans futur
  */
    
	public void annuler() {
            transfert(passe, futur);
	}

  /*
  *refaire un Jeu , on depile le Jeu dans futur , on l'execute
  * et on on l'empile dans passe
  */
        public void refaire() {
          transfert(futur, passe);
        }

  /*
  *faire un Jeu , on l'execute , on l'empile dans passe
  * et on vide futur
  */
    
	public void faire(Jeu nouveau) {
            Jeu c=(Jeu)nouveau.clone();
            passe.addFirst(c);
            futur.clear();
	}
}
