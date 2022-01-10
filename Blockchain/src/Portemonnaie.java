import java.util.ArrayList;

public class Portemonnaie {
    private Noeud user;
    private int nbMonneie;

    public Portemonnaie(Noeud user){
        this.user = user;
        this.nbMonneie = 0;
    }

    public void add(int nbMonnaie){
        nbMonneie += nbMonnaie;
    }

    public void reduce(int nbMonnaie){
        nbMonneie -= nbMonnaie;
    }

    public int getNbMonneie(){
        return nbMonneie;
    }

}
