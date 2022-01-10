public class Noeud {
    private int id;
    private String nom;
    private static int cpt = 0;
    private Portemonnaie portemonnaie;
    private Blockchain bitcoin;

    public Noeud(String nom) {
        this.id = cpt++;
        this.nom = nom;
        this.portemonnaie = new Portemonnaie(this);
    }

    public void acheter(int nbMonnaie){
        portemonnaie.add(nbMonnaie);
        bitcoin.newTransaction(nom+" a achete "+nbMonnaie+" bitcoins");
    }

    public void vendre(int nbMonnaie){
        if(portemonnaie.getNbMonneie() < nbMonnaie)
            System.out.println(nom+" n'a pas assez de monnaie pour vendre");
        portemonnaie.reduce(nbMonnaie);
        bitcoin.newTransaction(nom+" a vendu "+nbMonnaie+" bitcoins");
    }



}
