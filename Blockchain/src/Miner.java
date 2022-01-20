import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Miner extends Node implements Runnable {

    private final List<Transaction> transactionBuffer = new ArrayList<>();
    private final int clientID;
    private Transaction transactionTempo;
    private int nonce;
    private int difficulty;
    private final Lock lock = new ReentrantLock();
    private final Condition conditionTran = lock.newCondition();
    private final Condition conditionBlock = lock.newCondition();
    private boolean receiptTran = false;
    private boolean receiptBlock = false;

    public Miner(String name, Network network) {
        super(name, network);
        difficulty = network.getDifficulty();
        clientID = new LightNode(name, network).getNodeId();
        network.addNode(this);
    }

    public boolean verifySignature(Transaction transaction) throws Exception {
        return RsaUtil.verify(transaction.toString(), transaction.getSignature(), network.getPkWithID(transaction.getFromID()));
    }

    public void mine() {
        int nonce = 0;
        while (!receiptBlock) {
            Block block = new Block(network.copyBlockchainFromFN().getLatestBlock(), transactionBuffer.toArray(new Transaction[0]));
            String hash = block.getHeader().calcHeaderHash(nonce++);
            String toBeCheckedSubList = hash.substring(0, difficulty);
            System.out.println(nodeId + " " + nonce + " " + hash);
            if (toBeCheckedSubList.equals("0".repeat(difficulty))) {
                network.broadcastBlock(block);
                return;
            }
        }
    }

    public void receiptTransaction(Transaction transaction) {
        lock.lock();
        try {
            transactionTempo = transaction;
            receiptTran = true;
            conditionTran.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void receiptBlock() {
        receiptBlock = true;
    }
/*
    public String mineBlock(String s) {
        //""" MINE THE BLOCK TILL THE FIRST N LETTERS ARE 0.""
        while (true) {
            nonce = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
            String test_hash = s + nonce;
            test_hash = HashUtil.SHA256(test_hash);
            String toBeCheckedSubList = test_hash.substring(0, nbOfZeros);
            if (toBeCheckedSubList.equals("0".repeat(nbOfZeros))) {
                return test_hash;
            }
        }
    }

 */

    @Override
    public void run() {
        new Thread(() -> {
            while (true) {
                lock.lock();
                try {
                    while (!receiptTran) {
                        conditionTran.await();
                    }
                    receiptTran = false;
                    if (verifySignature(transactionTempo)) {
                        transactionBuffer.add(transactionTempo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }).start();

        while (!receiptBlock) {
            lock.lock();
            try {
                while (!receiptTran && transactionBuffer.isEmpty()) {
                    conditionTran.await(
                    );
                }
                mine();
                System.out.println("finish");
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
