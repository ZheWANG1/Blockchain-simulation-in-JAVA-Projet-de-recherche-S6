import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Validator extends Node{
    private final List<Transaction> transactionBuffer = new CopyOnWriteArrayList<>();
    private final int clientID = 0;
    private LightNode validator = null;
    private final int nbMax  = 10;


    public Validator(String name, Network network) {
        super(name, network, new Blockchain());
        chooseValidator();
    }

    public void chooseValidator(){
        /**
         * Function which choose a validator in order to guess a new block
         */
        List<Node> listNode = network.getNetwork(); // List of nodes in the network
        LightNode candidate = null;

        double probability = 0;// Probability
        for (Node node : listNode){ // For each nodes in the network
            if(node instanceof LightNode) { // If found node is an LightNode
                double stakeAmount = ((LightNode)node).getStakeAmount(); // Get LightNode's stakeAmount
                double stakeTime = ((LightNode)node).getStakeTime(); // Get LightNode's stakeTime (How long the node have been Staking)
                if (stakeAmount + stakeTime >= probability){ // il faut modifier // Random acces sim ?
                    candidate = ((LightNode) node); // The candidate to guess another block
                    probability = stakeAmount + stakeTime; // ?
                }
            }
        }
        validator = candidate;
    }

    public boolean verifySignature(Transaction transaction) throws Exception {
        return RsaUtil.verify("", transaction.getSignature(), network.getPkWithID(transaction.getFromID()));
    }

    public void validate() {
        new Thread(()->{
        while(true) {
            if (transactionBuffer.size() >= nbMax) { // If more than nbMax transaction has been received
                int i = 0;
                while (i != nbMax) // while i isn't equal to nbMaw
                    try {
                        if (verifySignature(transactionBuffer.get(i))) { // Verify the signature of ith transaction
                            i++;
                        } else {
                            System.out.println(transactionBuffer.get(i) + "is false"); // The transaction is fraudulent
                            transactionBuffer.remove(i); // Remove fraudulent transaction
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
            // List of transaction which can enter the next block
            List<Transaction> transactionsInBlock = transactionBuffer.subList(0, nbMax - 1);
            // Creation of the new block
            Block block = new Block(blockchain.getLatestBlock(), transactionsInBlock);
            // Guess of the hash
            String hash = block.getHeader().calcHeaderHash(0);
            block.getHeader().setHeaderHash(hash);
            //System.out.println(name + " " + hash);
            try {
                block.setNodeID(nodeId);
                network.broadcastBlock(block, RsaUtil.sign("", this.privateKey), nodeId, blockchain);
            } catch (Exception e) {
                e.printStackTrace();
            }
            transactionBuffer.removeAll(transactionsInBlock);
            chooseValidator();
        }}).start();
    }

    public void receiptTransaction(Transaction transaction) {
        transactionBuffer.add(transaction);
    }

    @Override
    public void receiptBlock(Block b, String signature, int nodeID, Blockchain blk) {

    }
}

