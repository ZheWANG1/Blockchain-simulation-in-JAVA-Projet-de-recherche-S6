package Blockchain;

import Network.Network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class Blockchain
 * o : Object -> Used for synchronization
 * cpt : int -> Used for blockid
 * blockid : int -> Identifier of a block
 * blkchain : List<Block> -> List of block representing the Block-chain
 */
public class Blockchain {

    private static final Object o = new Object();
    private static int cpt = 0;
    private final int blockChainId;
    private final List<Block> blkchain = new CopyOnWriteArrayList<>();
    private final Network network;

    /**
     * Constructor Blockchain
     * Create an empty Blockchain
     */
    public Blockchain(Network network) {
        synchronized (o) {
            blockChainId = cpt++;
            blkchain.addAll(createFirstBlock());
        }
        this.network = network;
    }

    /**
     * Function which create the first blockchain's block
     *
     * @return genesisBlock -> First block of the blockchain
     */
    public List<Block> createFirstBlock() {
        Block firstBlock = new Block(network.TYPE1);
        firstBlock.setNodeAddress("Master");
        Block secondBlock = new Block(firstBlock, network.TYPE2);
        firstBlock.setNodeAddress("Master");
        return Arrays.asList(firstBlock, secondBlock);
    }

    /**
     * Function which return the latest blockchain's block
     *
     * @return latest blockchain's block
     */
    public Block getLatestBlock() {
        return blkchain.get(blkchain.size() - 1);
    }

    public Block searchPrevBlockByID(String blockID, int i){
        if (i < 0){
            return null;
        }
        Block b = this.blkchain.get(i);
        if (blockID.equals(b.getBlockID()))
            return b;
        else{
            return searchPrevBlockByID(blockID, --i);
        }
    }


    /**
     * Function which add a block into the blockchain
     *
     * @param block block
     */
    public synchronized void addBlock(Block block) {
        blkchain.add(block);
    }

    /**
     * Function which print all blockchain's information
     */
    public void printBlk() {
        for (Block block : blkchain) {
            System.out.print(block.toString());
        }
    }

    /**
     * Function which return blockchain's size
     *
     * @return blockchain's size
     */
    public int getSize() {
        return blkchain.size();
    }

    /**
     * Function which return the blockchain's 6th valid block
     *
     * @return Block or null if the blockchain's size is lesser than 6
     */
    public Block getUpdateBlock() {
        if (blkchain.size() > 6)
            return blkchain.get(blkchain.size() - 6);
        return null;
    }

    /**
     * Function which return the blockchain's copy
     *
     * @return blockchain's copy
     */
    public Blockchain copyBlkch() {
        Blockchain blk = new Blockchain(network);
        blk.blkchain.clear();
        blk.blkchain.addAll(this.blkchain);
        return blk;
    }

}
