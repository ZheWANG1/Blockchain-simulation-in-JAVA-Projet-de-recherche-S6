public class Miner implements Runnable {
    private static int cpt = 0;
    private final int id;
    private final String nom;
    private final Wallet wallet;
    private final Blockchain associatedBlk;

    public Miner(String nom, Blockchain ABlk) {
        this.id = cpt++;
        this.nom = nom;
        this.wallet = new Wallet(this);
        this.associatedBlk = ABlk;
    }

    public void buy(double nbMoney) {
        wallet.add(nbMoney);
        associatedBlk.newTransactionPoS(nom + " a achete " + nbMoney + " bitcoins", nbMoney);
    }

    public void sell(double nbMoney) {
        if (wallet.getAccount() < nbMoney) {
            System.out.println(nom + " n'a pas assez de monnaie pour vendre");
            System.out.println("Rejected transaction");
            return;
        }
        wallet.reduce(nbMoney);
        associatedBlk.newTransactionPoS(nom + " a vendu " + nbMoney + " bitcoins", nbMoney);
    }

    public void electedToAddBlock(Block lastBlock, String transaction, double reward) {
        System.out.print("\n" + nom + " has been elected\n");
        Block newBlock = new Block(lastBlock, transaction);
        associatedBlk.addBlock(newBlock);
        wallet.add(reward);
    }

    public double getMoney() {
        return wallet.getAccount();
    }

    public String getName() {
        return nom;
    }

    public String toString() {
        return "\nNode " + id + " Owner : " + nom + " Account balance : " + getMoney() + "\n";
    }

    @Override
    public void run() {

    }
}
