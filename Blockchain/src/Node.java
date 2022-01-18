import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

public class Node{
    private static final Object o = new Object();
    private static int cpt = 0;
    protected final int nodeId;
    protected final String name;
    private Network network;


    public Node(String name) {
        synchronized (o) {
            this.nodeId = cpt++;
        }
        this.name = name;

    }

    public Node(String name, Network network) {
        this(name);
        this.network = network;
    }
}
