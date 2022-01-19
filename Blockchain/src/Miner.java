import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Miner extends Node implements Runnable {

    private List<Transaction> transactionBuffer = new ArrayList<>();
    private Transaction transactionTempo;
    private int clientID;
    private int nonce;
    private int difficulty;
    private Lock lock = new ReentrantLock();
    private Condition conditionTran = lock.newCondition();
    private Condition conditionBlock = lock.newCondition();
    private boolean receiptTran = false;
    private boolean receiptBlock = false;

    public Miner(String name, Network network) {
        super(name, network);
    }

    public boolean verifySignature(Transaction transaction) throws Exception {
        return RsaUtil.verify(transaction.toString(), transaction.getSignature(), network.getPkWithID(transaction.getFromID()));
    }

    public void mine() {

    }

    public void receiptTransaction(Transaction transaction) {
        try {
            if(verifySignature(transaction))
            transactionBuffer.add(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiptBlock(){

    }

    @Override
    public void run() {


    }
}
