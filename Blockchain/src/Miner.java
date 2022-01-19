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
        System.out.println(network.getPkWithID(transaction.getFromID()));
        return RsaUtil.verify(transaction.toString(), transaction.getSignature(), network.getPkWithID(transaction.getFromID()));
    }

    public void mine() {

    }

    public void receiptTransaction(Transaction transaction) {
        lock.lock();
        try{
            transactionTempo = transaction;
            receiptTran = true;
            conditionTran.signalAll();
        } finally {
            lock.unlock();
        }

    }

    public void receiptBlock() {

    }

    @Override
    public void run() {

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

        System.out.println(getNodeId()+"finish");


    }
}
