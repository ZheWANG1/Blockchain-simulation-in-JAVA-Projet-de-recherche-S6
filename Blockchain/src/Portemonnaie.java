import java.util.ArrayList;

public class Portemonnaie {
    private Noeud user;
    private double compte;

    public Portemonnaie(Noeud user){
        this.user = user;
        this.compte = 100;
    }

    public void add(double compte){
        this.compte += compte;
    }

    public void reduce(double nbMonnaie){
    	this.compte -= compte;
    }

    public double getCompte(){
        return compte;
    }

}
