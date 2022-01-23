import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

abstract class Node {
    private static final Object o = new Object();
    private static int cpt = 0;
    protected final int nodeId;
    protected final String name;
    protected Network network;
    protected Blockchain blockchain;
    protected PublicKey publicKey;
    protected PrivateKey privateKey;
    protected KeyPair keys;

    public Node(String name, Network network, Blockchain blk) {
        synchronized (o) {
            this.nodeId = cpt++;
        }
        blockchain = blk;
        this.name = name;
        this.network = network;
    }
    
    public abstract void receiptBlock(Block b);
    
    public Integer getNodeId() {
        return nodeId;
    }

    public Blockchain getBlockchain(){
        return blockchain;
    }


    public PublicKey getPublicKey() {
        return publicKey;
    }
}
