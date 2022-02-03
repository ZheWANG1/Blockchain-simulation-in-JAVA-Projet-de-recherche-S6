package PoS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class PoS.Validator
 * transactionBuffer : List<Transaction> -> A list of transactions which are waiting processing
 * nbMax : int -> Amount maximum of transactions in a block
 * validator : LightNode -> the light node which is elected as a validator
 * transactionTempo : Transaction -> Currently a received transaction
 * lock : concurrent.locks.Lock -> Technique for implement concurrent program
 * receiptTrans : boolean -> ?
 * condition : concurrent.locks.condition -> Technique for implement concurrent program
 */
public class Validator extends Node {
    private final List<Transaction> transactionBuffer = new CopyOnWriteArrayList<>();
    private final int nbMax = 10;
    private LightNode validator = null;
    private Transaction transactionTempo = null;
    private final Lock lock = new ReentrantLock();
    private boolean receiptTrans = false;
    private final Condition condition = lock.newCondition();


    public Validator(Network network) {
        super("", network, new Blockchain());
        network.addNode(this); //?
        validate();
    }

    /**
     * Function which choose a validator in order to guess a new block
     */
    public void chooseValidator() {
        List<Node> listNode = network.getNetwork(); // List of nodes in the network

        Map<LightNode, Double> mapProba = new HashMap<>();
        for (Node node : listNode) { // For each node in the network
            if (node instanceof LightNode) { // If found node is an PoW.LightNode
                double stakeAmount = ((LightNode) node).getStakeAmount(); // Get PoW.LightNode's stakeAmount
                double stakeTime = System.currentTimeMillis() - ((LightNode) node).getStakeTime(); // Get PoW.LightNode's stakeTime (How long the node have been Staking)
                mapProba.put((LightNode) node, stakeAmount * stakeTime);
            }
        }
        double sum = 0;
        for (Map.Entry<LightNode, Double> entry : mapProba.entrySet()) {
            sum += entry.getValue();
        }

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
        return RsaUtil.verify("", transaction.getSignature(), network.getPkWithID(transaction.getFromID()));
    }

    public void validate() {
        new Thread(() -> {
            while (true) {
                lock.lock();
                try {
                    if (validator != null) {
                        long end = 0;
                        long start = System.currentTimeMillis();
                        while (transactionBuffer.size() < nbMax) { // If more than nbMax transaction has been received
                            try {

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
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            end = System.currentTimeMillis();
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
                            System.out.println(this.name + " broadcast block");
                            network.broadcastBlock(block, RsaUtil.sign("", validator.privateKey), validator.nodeId, blockchain);
                            System.out.println("Broadcast finished");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        transactionBuffer.removeAll(transactionsInBlock);
                        validator.setValidator(null);
                    }
                    chooseValidator();
                } finally {
                    lock.unlock();
                }
            }
        }).start();
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
    public void receiptBlock(Block b, String signature, int nodeID, Blockchain blk) {
        blockchain.addBlock(b);
    }
}

