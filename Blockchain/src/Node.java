import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

public class Node implements Runnable {
    private static final Object o = new Object();
    private static int cpt = 0;
    private final int nodeId;
    private final String nom;
    private final Blockchain associatedBlk;
    private double wallet;
    private Network network;
    private KeyPair keys;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private boolean isMiner;
    private List<Transaction> listTransaction;

    public Node(String nom, Blockchain ABlk) {
        synchronized (o) {
            this.nodeId = cpt++;
        }
        this.nom = nom;
        this.wallet = 0;
        this.associatedBlk = ABlk;
        this.isMiner = true;
        try {
            keys = RsaUtil.generateKeyPair();
            publicKey = keys.getPublic();
            privateKey = keys.getPrivate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Node(String nom, Blockchain ABlk, boolean isMiner, Network network) {
        this(nom, ABlk);
        this.isMiner = isMiner;
        this.network = network;
    }

    public void sendMoneyTo(double amount, int nodeId) {
        if (wallet < amount) {
            System.out.println(nom + " n'a pas assez de monnaie pour vendre");
            System.out.println("Rejected transaction");
        } else {
            network.broadcast(new Transaction("", this.nodeId, nodeId, amount, System.currentTimeMillis(), ""));
        }
    }

    public double getWallet() {
        return wallet;
    }

    public boolean verifySign() {
        return true;
    }

    @Override
    public void run() {

    }

}
