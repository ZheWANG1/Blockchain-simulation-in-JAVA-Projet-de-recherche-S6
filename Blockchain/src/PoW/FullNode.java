package PoW;

import Blockchain.Block;
import Blockchain.Blockchain;
import Network.Network;

/**
 * Class FullNode extending Miner
 * This class simulate a node which download the whole blockchain acting as one of the network's server.
 */
public class FullNode extends Miner {
    private final boolean isMiner;

    /**
     * Constructor FullNode
     *
     * @param nom     Name of the FullNode
     * @param network Network of the FullNode, one node is connected to the whole network (P2P)
     */
    public FullNode(String nom, Network network, boolean isMiner) {
        super(nom, network);
        this.isMiner = isMiner;
    }

    @Override
    public void receiptBlock(Block block, String signature, String nodeAddress, Blockchain blk) {
        if (isMiner) {
            super.receiptBlock(block, signature, nodeAddress, blk);
        } else {
            this.blockchain.addBlock(block);
        }
    }
}
