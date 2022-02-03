 package PoS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

 /**
  * Class Validator
  * transactionBuffer : List<Transaction> -> A list of transactions which are waiting processing
  * nbMax : int -> Amount maximum of transactions in a block
  * validator : LightNode -> the light node which is elected as a validator
  * transactionTempo : Transaction -> Currently a received transaction
  * lock : concurrent.locks.Lock -> Technique for implement concurrent program
  * receiptTrans : boolean -> a transaction is received
  * condition : concurrent.locks.condition -> Technique for implement concurrent program
  */
public class Validator implements Runnable {
    private final static int NB_Max = 10;
    private final List<Transaction> transactionBuffer = new CopyOnWriteArrayList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final Network network;
    private LightNode validator = null;
    private Transaction transactionTempo = null;
    private boolean receiptTrans = false;
    private String name;

    public Validator(Network network) {
        this.network = network;
    }

    /**
     * Function which choose a validator in order to guess a new block
     */
    public void chooseValidator() {
        List<Node> listNode = network.getNetwork(); // List of nodes in the network

        Map<LightNode, Double> mapProba = new HashMap<>();
        for (Node node : listNode) { // For each node in the network
            if (node instanceof LightNode) { // If found node is an LightNode
                double stakeAmount = ((LightNode) node).getStakeAmount(); // Get LightNode's stakeAmount
                double stakeTime = System.currentTimeMillis() - ((LightNode) node).getStakeTime(); // Get LightNode's stakeTime (How long the node have been Staking)
                mapProba.put((LightNode) node, stakeAmount * stakeTime);
            }
        }
        double sum = mapProba.values().stream().mapToDouble(v -> v).sum();

        if (sum == 0) // if anyone didn't de posit bitcoin as stake
            return;

        double numberRandom = Math.random() * sum;
        for (Map.Entry<LightNode, Double> entry : mapProba.entrySet()) {
            numberRandom -= entry.getValue();
            if (numberRandom < 0) {
                validator = entry.getKey();
                this.name = validator.name;
                System.out.println(validator.name + " is chosen");
                validator.setValidator(this);
                break;
            }
        }
    }

    public boolean verifySignature(Transaction transaction) throws Exception {
        return RsaUtil.verify(transaction.toString(), transaction.getSignature(), network.getPkWithID(transaction.getFromID()));
    }

    public void validate() {
        boolean interrupt = false;
        while (!interrupt) {
            lock.lock();
            try {
                if (validator != null) {
                    Blockchain blkchainTempo = network.copyBlockchainFromFN();
                    while (transactionBuffer.size() < NB_Max) { // If more than nbMax transaction has been received
                        if (!receiptTrans) {
                            condition.await();
                        }
                        transactionBuffer.add(transactionTempo);
                        receiptTrans = false;

                        int index = transactionBuffer.size() - 1;
                        if (!verifySignature(transactionBuffer.get(index))) { // Verify the signature of ith transaction
                            System.out.println(transactionBuffer.get(index) + "is fraudulent"); // The transaction is fraudulent
                            transactionBuffer.remove(index); // Remove fraudulent transaction
                        }
                    }
                    // List of transaction which can enter the next block
                    List<Transaction> transactionsInBlock = transactionBuffer.subList(0, NB_Max - 1);
                    // Creation of the new block
                    Block block = new Block(blkchainTempo.getLatestBlock(), transactionsInBlock);
                    // Guess of the hash
                    String hash = block.getHeader().calcHeaderHash(0);
                    block.getHeader().setHeaderHash(hash);

                    block.setNodeID(validator.getNodeId());
                    System.out.println(this.name + " broadcast block");
                    network.broadcastBlock(block, RsaUtil.sign(block.toString(), validator.privateKey), validator.nodeId, blkchainTempo);
                    System.out.println("Broadcast finished");

                    transactionBuffer.removeAll(transactionsInBlock);
                    validator.setValidator(null);
                }
                chooseValidator();
            } catch (Exception e) {
                e.printStackTrace();
                interrupt = true;
            } finally {
                lock.unlock();
            }
        }
    }

    public void receiptTransaction(Transaction transaction) {
        lock.lock();
        try {
            transactionTempo = transaction;
            receiptTrans = true;
            condition.signalAll();
        } catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        validate();
    }
}