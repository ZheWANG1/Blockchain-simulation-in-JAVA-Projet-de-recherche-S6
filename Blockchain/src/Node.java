abstract class Node {
    private static final Object o = new Object();
    private static int cpt = 0;
    protected final int nodeId;
    protected final String name;
    protected Network network;

    public Node(String name, Network network) {
        synchronized (o) {
            this.nodeId = cpt++;
        }
        this.name = name;
        this.network = network;
    }
    
    public abstract void receiptBlock(Block b);
    
    public Integer getNodeId() {
        return nodeId;
    }
}
