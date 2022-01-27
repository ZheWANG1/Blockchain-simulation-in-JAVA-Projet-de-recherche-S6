import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Validator extends Node {
    private final List<Transaction> transactionBuffer = new CopyOnWriteArrayList<>();
    private final int clientID = 0;
    private final int nbMax = 10;
    private LightNode validator = null;
    private Transaction transactionTempo = null;
    private ReentrantLock lock = new ReentrantLock();
    private boolean receiptTrans = false;


    public Validator(Network network) {
        super("", network, new Blockchain());
        chooseValidator();

    }

    public void chooseValidator() {
        /**
         * Function which choose a validator in order to guess a new block
         */
        List<Node> listNode = network.getNetwork(); // List of nodes in the network
        LightNode candidate = null;

        double probability = 0;// Probability
        Map<LightNode, Double> mapProba = new HashMap<>();
        for (Node node : listNode) { // For each nodes in the network
            if (node instanceof LightNode) { // If found node is an LightNode
                double stakeAmount = ((LightNode) node).getStakeAmount(); // Get LightNode's stakeAmount
                double stakeTime = System.currentTimeMillis() - ((LightNode) node).getStakeTime(); // Get LightNode's stakeTime (How long the node have been Staking)
                mapProba.put((LightNode) node, stakeAmount * stakeTime);
            }
        }
        double sum = 0;
        for (Map.Entry<LightNode, Double> entry : mapProba.entrySet()) {
            sum += entry.getValue();
        }

        double numberRandom = Math.random() * sum;
        for (Map.Entry<LightNode, Double> entry : mapProba.entrySet()) {
            numberRandom -= entry.getValue();
            if (numberRandom < 0) {
                validator = entry.getKey();
                this.name = validator.name;
                break;
            }
        }
    }

    public boolean verifySignature(Transaction transaction) throws Exception {
        return RsaUtil.verify("", transaction.getSignature(), network.getPkWithID(transaction.getFromID()));
    }

    public void validate() {
        new Thread(() -> {
            while (true) {
                lock.lock();
                while (transactionBuffer.size() < nbMax) { // If more than nbMax transaction has been received
                        try {

                            if (!receiptTrans){
                                wait();
                            }
                            transactionBuffer.add(transactionTempo);
                            receiptTrans = false;

                            int index = transactionBuffer.size()-1;
                            if (!verifySignature(transactionBuffer.get(index))) { // Verify the signature of ith transaction
                                System.out.println(transactionBuffer.get(index) + "is false"); // The transaction is fraudulent
                                transactionBuffer.remove(index); // Remove fraudulent transaction
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
            }
        }).start();
    }

    public void receiptTransaction(Transaction transaction) {
        lock.lock();
        try {
            transactionTempo = transaction;
            receiptTrans = true;
            notifyAll();
        }finally {
            lock.unlock();
        }

    }

    @Override
    public void receiptBlock(Block b, String signature, int nodeID, Blockchain blk) {

    }
}

