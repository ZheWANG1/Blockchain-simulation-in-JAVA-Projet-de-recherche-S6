package Network;

import Blockchain.Block;
import Blockchain.Blockchain;
import MessageTypes.Transaction;
import PoS.LightNode;
import Utils.RsaUtil;

import java.util.ArrayList;
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
    private static final int TIME_TO_WAIT = 1000; // 1 sec
    private final static int SLOTS_MAX = 10;
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
                mapProba.put((LightNode) node, stakeAmount * (stakeTime));
            }
        }
        double sum = mapProba.values().stream().mapToDouble(v -> v).sum();
        int number_of_slots = 0;
        for (Node node : listNode) {
            if (node instanceof LightNode) {
                number_of_slots += (mapProba.get(node) / sum) * 10;
            }
        }
        System.out.println("Slots : " + number_of_slots);
        int node_slots;
        List<LightNode> lightNodeSlots = new ArrayList<>(number_of_slots);
        for (int j = 0; j < number_of_slots; j++)
            lightNodeSlots.add(null);
        for (Node node : listNode) {
            if (node instanceof LightNode) {
                node_slots = (int) ((mapProba.get(node) / sum) * 10);
                int slot_index;
                for (int i = 0; i < node_slots; i++) {
                    do {
                        slot_index = (int) (Math.random() * number_of_slots);
                    }
                    while (lightNodeSlots.get(slot_index) != null);
                    lightNodeSlots.set(slot_index, (LightNode) node);
                }
            }
        }
        System.out.print("\n[");
        for (LightNode ln : lightNodeSlots) {
            System.out.print(ln.name + " ");
        }
        System.out.print("]\n");


        if (sum == 0) // if anyone didn't deposit bitcoin as stake
            return;

        int chosen_node_index = (int) (Math.random() * number_of_slots);
        validator = lightNodeSlots.get(chosen_node_index);
        this.name = validator.name;
        System.out.println(validator.name + " is chosen");
        validator.setValidator(this);

        /*
        double numberRandom = Math.random();

        for (Map.Entry<LightNode, Double> entry : mapProba.entrySet()) {
            numberRandom -= entry.getValue() / sum;
            if (numberRandom < 0) {
                validator = entry.getKey();
                this.name = validator.name;
                System.out.println(validator.name + " is chosen");
                validator.setValidator(this);
                break;
            }
        }
        */
    }

    public boolean verifySignature(Transaction transaction) throws Exception {
        return RsaUtil.verify(transaction.toString(), transaction.getSignature(), network.getPkWithAddress(transaction.getFromAddress()));
    }

    public void validate() {
        boolean interrupt = false;
        while (!interrupt) {
            lock.lock();
            long start = System.currentTimeMillis();
            try {
                if (validator != null) {
                    Blockchain blkchainTempo = network.copyBlockchainFromFN();
                    long end = start;
                    while (end - start < TIME_TO_WAIT) {
                        if (!receiptTrans) {
                            condition.await();
                        }
                        transactionBuffer.add(transactionTempo);

                        int index = transactionBuffer.size() - 1;
                        if (!verifySignature(transactionBuffer.get(index))) { // Verify the signature of ith transaction
                            System.out.println(transactionBuffer.get(index) + "is fraudulent"); // The transaction is fraudulent
                            transactionBuffer.remove(index); // Remove fraudulent transaction
                        }
                        end = System.currentTimeMillis();
                    }
                    // List of transaction which can enter the next block
                    int size_tr_in_block;
                    if (transactionBuffer.size() >= NB_Max) {
                        size_tr_in_block = NB_Max - 1;
                    } else {
                        size_tr_in_block = transactionBuffer.size();
                    }
                    List<Transaction> transactionsInBlock = transactionBuffer.subList(0, size_tr_in_block);
                    // Creation of the new block
                    Block block = new Block(blkchainTempo.getLatestBlock(), transactionsInBlock);
                    // Guess of the hash
                    String hash = block.getHeader().calcHeaderHash(0, block.getFooter().getPrevHash());
                    block.getHeader().setHeaderHash(hash);

                    block.setNodeAddress(validator.getNodeAddress());
                    System.out.println(this.name + " broadcast block");
                    network.broadcastBlock(block, RsaUtil.sign(block.toString(), validator.privateKey), validator.nodeAddress, blkchainTempo);
                    System.out.println("Broadcast finished");
                    receiptTrans = false;

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
            System.out.print("Transaction received : " + transaction.toString());
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