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
        Block genesisBlock = new Block("First ever transaction");
        return genesisBlock;
    }

    public Block getLatestBlock(){
        return blkchain.get(blkchain.size()-1);
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
    
    public void newTransaction(String transaction) {
    	Block lastBlock = getLatestBlock();
    	Block newBlock = new Block(lastBlock, transaction);
    	addBlock(newBlock);	
    	if (!chainValidation()) {
    		System.out.print("Erreur block invalide\n");
    	}else {
    		System.out.print("Transaction ajoute avec succes\n");
    	}
    }
    
    public void printBlk() {
   	 Iterator<Block> it = blkchain.iterator();
        while (it.hasNext()){
          System.out.print(it.next().toString());
        }
   }
   
}
