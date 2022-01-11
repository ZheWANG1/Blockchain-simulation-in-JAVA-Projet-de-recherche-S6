public class Noeud {
    private static int cpt = 0;
    private final int id;
    private final String nom;
    private final Portemonnaie portemonnaie;
    private final Blockchain associatedBlk;

    public Noeud(String nom, Blockchain ABlk) {
        this.id = cpt++;
        this.nom = nom;
        this.portemonnaie = new Portemonnaie(this);
        this.associatedBlk = ABlk;
    }

    public void acheter(double nbMonnaie) {
        portemonnaie.add(nbMonnaie);
        associatedBlk.newTransactionPoS(nom + " a achete " + nbMonnaie + " bitcoins", nbMonnaie);
    }

    public void vendre(double nbMonnaie) {
        if (portemonnaie.getCompte() < nbMonnaie) {
            System.out.println(nom + " n'a pas assez de monnaie pour vendre");
            System.out.println("Rejected transaction");
            return;
        }
        portemonnaie.reduce(nbMonnaie);
        associatedBlk.newTransactionPoS(nom + " a vendu " + nbMonnaie + " bitcoins", nbMonnaie);
    }

    public void electedToAddBlock(Block lastBlock, String transaction, double reward) {
        System.out.print("\n" + nom + " has been elected\n");
        Block newBlock = new Block(lastBlock, transaction);
        associatedBlk.addBlock(newBlock);
        portemonnaie.add(reward);
    }

    public double getMoney() {
        return portemonnaie.getCompte();
    }

    public String getName() {
        return nom;
    }

    public String toString() {
        return "\nNode " + id + " Owner : " + nom + " Account balance : " + getMoney() + "\n";
    }


}
