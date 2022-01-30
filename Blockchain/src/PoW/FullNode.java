package PoW;

/**
 * Class FullNode extending Miner
 *         This class simulate a node which download the whole blockchain acting as one of the network's server.
 */
public class FullNode extends Miner {
    private boolean isMiner;
    /**
     * Constructor PoW.FullNode
     * @param nom String -> Name of the PoW.FullNode
     * @param network -> PoW.Network of the PoW.FullNode, one node is connected to the whole network (P2P)
     * */
    public FullNode(String nom, Network network, boolean isMiner) {
        super(nom,network);
        //network.addNode(this);
        this.isMiner = isMiner;
    }

    @Override
    public void receiptBlock(Block block, String signature, int nodeID, Blockchain blk){
        if(isMiner){
            super.receiptBlock(block,signature,nodeID,blk);
        }else {
            this.blockchain.addBlock(block);
        }
    }

}
