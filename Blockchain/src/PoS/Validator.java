 package PoS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Validator extends Node implements Runnable {
    private final List<Transaction> transactionBuffer = new CopyOnWriteArrayList<>();
    private final int nbMax = 10;
    private LightNode validator = null;
    private Transaction transactionTempo = null;
    private final Lock lock = new ReentrantLock();
    private boolean receiptTrans = false;
    private final Condition condition = lock.newCondition();


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
        return RsaUtil.verify("", transaction.getSignature(), network.getPkWithID(transaction.getFromID()));
    }

    public void validate() {
        while (true) {
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
                    List<Transaction> transactionsInBlock = transactionBuffer.subList(0, nbMax - 1);
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
            } catch (Exception ignore) {

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

