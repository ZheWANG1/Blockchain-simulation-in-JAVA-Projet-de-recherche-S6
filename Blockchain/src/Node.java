import java.nio.channels.NetworkChannel;
import java.util.List;
import java.util.concurrent.TransferQueue;

public class Node implements Runnable {
    private static final Object o = new Object();
    private static int cpt = 0;
    private final int idNode;
    private final String nom;
    private final Wallet wallet;
    private final Blockchain associatedBlk;
    private Network network;
    private String publicKey, privateKey;
    private boolean isMiner;
    private List<Object> listTransaction;

    public Node(String nom, Blockchain ABlk) {
        synchronized (o){
            this.idNode = cpt++;
        }
        this.nom = nom;
        this.wallet = new Wallet(this);
        this.associatedBlk = ABlk;
        this.isMiner = true;
    }

    public Node(String nom, Blockchain ABlk, boolean isMiner, Network network) {
        this(nom, ABlk);
        this.isMiner = isMiner;
        this.network = network;
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
        return "\nNode " + idNode + " Owner : " + nom + " Account balance : " + getMoney() + "\n";
    }

    @Override
    public void run() {

    }

}
