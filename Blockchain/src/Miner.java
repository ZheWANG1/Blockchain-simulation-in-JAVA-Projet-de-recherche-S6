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
    // Le mineur doit ajouter la transaction qui lui fais gagner de l'argent dans le block avant de miner
    // Le mineur gagnant broadcastera le block avec la transaction de son gain sans verification de la signature (Pour simplifier)
    // Le Network pendant le broadcast fera alors un update des wallets via la fonction : updateAllWallet
    // Donc le miner gagnant aura juste a broadcaster son Block et les wallets seront update

    public void receiptBlock(){

    }

    @Override
    public void run() {


    }
}
