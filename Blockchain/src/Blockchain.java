import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class Blockchain {

    private static final Object o = new Object();
    private static int cpt = 0;
    private final int blockcid;
    private final List<Block> blkchain = new CopyOnWriteArrayList<>();
    private final List<Miner> reseau = new ArrayList<>();
    private final double blockCreationfee = 0.10;

    public Blockchain() {
        synchronized (o) {
            blockcid = cpt++;
        }
        blkchain.add(createFirstBlock());
    }

    public Block createFirstBlock() {
        Block genesisBlock = new Block("First ever transaction");
        return genesisBlock;
    }

    public Block getLatestBlock() {
        return blkchain.get(blkchain.size() - 1);
    }

    public synchronized void addBlock(Block block) {
        blkchain.add(block);
    }

    public boolean chainValidation() {
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

    public void newTransactionPoW(String transaction) {
        Block lastBlock = getLatestBlock();
        Block newBlock = new Block(lastBlock, transaction);
        addBlock(newBlock);
        if (!chainValidation()) {
            System.out.print("Erreur block invalide\n");
        } else {
            System.out.print("Transaction ajoute avec succes\n");
        }
    }


    private Miner electionNoeud() {
        /*
         Naive function which elect the richest node to add the block
         */
        double maxAccount = 0;
        Miner richestNode = null;
        for (Miner miner : reseau) {
            double acc = miner.getMoney();
            if (acc > maxAccount) {
                richestNode = miner;
                maxAccount = acc;
            }
        }
        return richestNode;
    }


    public void newTransactionPoS(String transaction, double money) {
        Block lastBlock = getLatestBlock();
        Miner electedNode = electionNoeud();
        electedNode.electedToAddBlock(lastBlock, transaction, money * blockCreationfee);

        if (!chainValidation()) {
            System.out.print("Erreur block invalide\n");
        } else {
            System.out.print("Transaction ajoute avec succes\n");
        }
    }

    public void printBlk() {
        for (Block block : blkchain) {
            System.out.print(block.toString());
        }
    }

    public void printNodes() {
        for (Miner miner : reseau) {
            System.out.print(miner.toString());
        }
    }

    public void addNoeud(Miner n) {
        reseau.add(n);
    }


}
