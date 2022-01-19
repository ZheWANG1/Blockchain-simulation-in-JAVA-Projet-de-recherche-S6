import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class LightNode extends Node {
    private double wallet;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private KeyPair keys;
    private final LightBlockChain lightBlkch;

    public LightNode(String name, Network network) {
        super(name, network);
        this.wallet = 80;
        lightBlkch = new LightBlockChain();
        try {
            keys = RsaUtil.generateKeyPair();
            publicKey = keys.getPublic();
            privateKey = keys.getPrivate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        network.addNode(this);
    }

    public void sendMoneyTo(double amount, int nodeId) {
        if (wallet < amount) {
            System.out.println(name + " Not enough bitcoin to send"); // Whatever the currency
            System.out.println("Rejected transaction");
        } else {
            network.broadcastTransaction(new Transaction("", this.nodeId, nodeId, amount, System.currentTimeMillis(), 0.1, privateKey));
        }
    }

    public double getWallet() {
        return wallet;
    }

    public void receiptCoin(double amount) {
        wallet += amount;
        System.out.println(this.name + " received " + amount + " bitcoins");
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void receiptBlock(Block b) {
        lightBlkch.addLightHeader(b.getHeader());
    }
}
