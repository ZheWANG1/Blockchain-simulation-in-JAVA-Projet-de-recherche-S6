import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Blockchain {

    private static final Object o = new Object();
    private static int cpt = 0;
    private final int blockcid;
    private final List<Block> blkchain = new CopyOnWriteArrayList<>();

    public Blockchain() {
        synchronized (o){
            blockcid = cpt++;
        }
        blkchain.add(createFirstBlock());
    }

    public Block createFirstBlock(){
        Block genesisBlock = new Block();
        return genesisBlock;
    }

    public int getLatestBlock(){
        return blkchain.get(blkchain.size()-1).getBlockId();
    }

    public synchronized void addBlock(Block block){
        blkchain.add(block);
    }

    public boolean chainValidation(){
        Iterator<Block> it = blkchain.iterator();
        Block blockprev = it.next();
        while (it.hasNext()){
            Block block = it.next();
            if(block.getHeader().getPrevHash() != blockprev.getHeader().getHeaderHash())
                return false;
            blockprev = block;
        }
        return true;
    }
}
