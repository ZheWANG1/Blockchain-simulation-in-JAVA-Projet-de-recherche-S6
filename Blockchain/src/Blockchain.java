import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Blockchain {

    private static final Object o = new Object();
    private static int cpt = 0;
    private final int blockcid;
    private final List<Block> blkchain = new CopyOnWriteArrayList<>();
    private List<Noeud> reseau = new ArrayList<Noeud>();
    private double blockCreationfee = 0.10;
    
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
    
    public void newTransactionPoW(String transaction) {
    	Block lastBlock = getLatestBlock();
    	Block newBlock = new Block(lastBlock, transaction);
    	addBlock(newBlock);	
    	if (!chainValidation()) {
    		System.out.print("Erreur block invalide\n");
    	}else {
    		System.out.print("Transaction ajoute avec succes\n");
    	}
    }
    
    
    private Noeud electionNoeud() {
    	/*** 
    	 Naive function which elect the richest node to add the block
    	 */
    	double maxAccount = 0;
    	Noeud richestNode = null;
    	for (int i=0; i<reseau.size();i++) {
    		double acc = reseau.get(i).getMoney();
    		if (acc > maxAccount) {
    			richestNode = reseau.get(i);
    			maxAccount = acc;
    		}
    	}
		return richestNode;
    }
    
    
    public void newTransactionPoS(String transaction,double money) {
    	Block lastBlock = getLatestBlock();
    	Noeud electedNode = electionNoeud();
    	electedNode.electedToAddBlock(lastBlock, transaction, money*blockCreationfee);
    
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
    
    public void printNodes() {
    	Iterator<Noeud> it = reseau.iterator();
        while (it.hasNext()){
          System.out.print(it.next().toString());
        }
    }
    
    public void addNoeud(Noeud n) {
    	reseau.add(n);
    }
    
 
}
