import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

abstract class Node{
    private static final Object o = new Object();
    private static int cpt = 0;
    protected final int nodeId;
    protected final String name;
    private Network network;



    public Node(String name, Network network) {
    	synchronized (o) {
            this.nodeId = cpt++;
        }
        this.name = name;
        this.network = network;
        network.addNode(this);
    }

	public Integer getNodeId() {
		return nodeId;
	}
}
