import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

public class Node implements Runnable {
    private static final Object o = new Object();
    private static int cpt = 0;
    protected final int nodeId;
    protected final String nom;
    private Network network;
    private List<Transaction> listTransaction;

    public Node(String nom) {
        synchronized (o) {
            this.nodeId = cpt++;
        }
        this.nom = nom;

    }

    public Node(String nom, Network network) {
        this.nom = nom;
        this.network = network;
    }

    public void getBlock(){}

    public boolean verifySign() {
        return true;
    }

    public int getNodeId(){ return nodeId;}


    public void addTransaction(Transaction transaction){
        listTransaction.add(transaction);
    }

    @Override
    public void run() {

    }

}
