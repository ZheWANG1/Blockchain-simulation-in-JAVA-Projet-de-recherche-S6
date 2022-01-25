import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class Blockchain {
    /*
        Class Blockchain
        o : Object -> Used for synchronization
        cpt : int -> Used for blockid
        blockid : int -> Identifier of a block
        blkchain : List<Block> -> List of block representing the Block-chain
     */

    private static final Object o = new Object();
    private static int cpt = 0;
    private final int blockcid;
    private final List<Block> blkchain = new CopyOnWriteArrayList<>();


    public Blockchain() {
        /*
            Constructor Blockchain
            Create an empty Blockchain
         */
        synchronized (o) {
            blockcid = cpt++;
            blkchain.add(createFirstBlock());
        }
    }

    public Block createFirstBlock() {
        /*
            Function which create the first block in the blockchain
         */
        Block genesisBlock = new Block();
        return genesisBlock;
    }

    public Block getLatestBlock() {
        /*
            Function which return the latest block
         */
        return blkchain.get(blkchain.size() - 1);
    }

    public synchronized void addBlock(Block block) {
        /*
            Function which add a block into the blockchain
         */
        blkchain.add(block);
    }

    public boolean chainValidation() {
        /*
            Function which verify the validity of the blockchain
         */
        Iterator<Block> it = blkchain.iterator();
        Block blockprev = it.next();
        while (it.hasNext()) {
            Block block = it.next();
            if (!Objects.equals(block.getHeader().getPrevHash(), blockprev.getHeader().getHeaderHash()))
                return false;
            blockprev = block;
        }
        return true;
    }

    public void printBlk() {
        /*
            Function which print each block information
         */
        for (Block block : blkchain) {
            System.out.print(block.toString());
        }
    }

    public int getSize() {
        /*
            Function which return the number of blocks
         */
        return blkchain.size();
    }

    public Block getUpdateBlock() {
        /*
            Function which return the 6th block of the blockchain which is considered a valid block
         */
        if (blkchain.size() > 6)
            return blkchain.get(blkchain.size() - 6);
        return null;
    }

    public Blockchain copyBlkch() {
        /*
            Function which return the blockchain's copy
         */
        Blockchain blk = new Blockchain();
        blk.blkchain.clear();
        blk.blkchain.addAll(this.blkchain);
        return blk;
    }

}
