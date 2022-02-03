package PoS;

/**
 * Class FullNode extending Node
 * This class simulate a node which download the whole blockchain acting as one of the network's server.
 */
public class FullNode extends Node {

    /**
     * Constructor FullNode
     *
     * @param nom     Name of the FullNode
     * @param network PoS.Network of the FullNode, one node is connected to the whole network (P2P)
     */
    public FullNode(String nom, Network network) {
        super(nom, network, new Blockchain());
        network.addNode(this);
    }

    @Override
    public void receiptBlock(Block block, String signature, int nodeID, Blockchain blk) {
        this.blockchain.addBlock(block);
    }

}
