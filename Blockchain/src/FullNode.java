
public class FullNode extends Node {
    /*
        Class FullNode extending Node
        This class simulate a node which download the whole blockchain acting as one of the network's server.
     */

    public FullNode(String nom, Network network) {
        /*
            Constructor FullNode
            nom : String -> Name of the FullNode
            network : Network -> Network of the FullNode, one node is connected to the whole network (P2P)
         */
        super(nom,network, new Blockchain());
        network.addNode(this);
    }

    @Override
    public void receiptBlock(Block block, String signature, int nodeID, Blockchain blk){
        /*
            Function which receipt a new block mined/staked by a miner/validator
         */
        this.blockchain.addBlock(block);
    }

}
