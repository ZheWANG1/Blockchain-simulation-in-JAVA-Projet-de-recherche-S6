package Network;

import Blockchain.Block;
import MessageTypes.Transaction;
import PoS.FullNode;
import PoS.LightNode;
import Utils.RsaUtil;

import java.util.ArrayList;
import java.util.List;

/*
   Une node validator est comme une fullNode
   Sont objectif est de créer les blocks et de verifier les transactions
   Ainsi on verififira chaques blocs ainsi que chaque transaction
   Lorsque que ce validator sera choisi il forgera un block et le broadcastera celui-ci alors passera
   par le consensus et sera validée
   Des LightNode peuvent decider de miser sur la current fullNode
   Plus le stake misé est grand plus la validator node aura de probabilité d'être choisie
   la FullNode sera alors recompensée et les LightNode receveront une part du gain
 */
public class ValidatorNode extends FullNode {
    public static int MAX_TRANSACTION = 10;
    private final ArrayList<Transaction> pendingTransaction = new ArrayList<>();
    private final ArrayList<Transaction> fraudulentTransaction = new ArrayList<>();
    private final LightNode fullNodeAccount;
    private final ArrayList<String> investorList = new ArrayList<>();

    /**
     * Constructor FullNode
     *
     * @param nom     Name of the FullNode
     * @param network Network of the FullNode, one node is connected to the whole network (P2P)
     */
    public ValidatorNode(String nom, Network network, LightNode fullNodeAccount) {
        super(nom, network);
        this.fullNodeAccount = fullNodeAccount;
    }

    public void receiptTransaction(Transaction t) throws Exception {
        boolean transactionStatus = verifyTransaction(t);
        if (transactionStatus) {
            System.out.println("Transaction" + t.getTransactionID() + " receipt by " + this.name + " and accepted");
            pendingTransaction.add(t);
        } else {
            System.out.println("Transaction" + t.getTransactionID() + " receipt by " + this.name + " but refused (Probably fraudulent)");
            fraudulentTransaction.add(t);
        }
    }

    public boolean verifyTransaction(Transaction t) throws Exception {
        return RsaUtil.verify(t.toString(), t.getSignature(), network.getPkWithAddress(t.getFromAddress()));
    }

    public void updateTransactionList(Block b) {
        List<Transaction> lt = b.getTransaction();
        for (Transaction t : lt) {
            pendingTransaction.remove(t);
        }
        System.out.println("Transaction list of " + this.name + " succesfully updated");

    }

    public void forgeBlock() {
        for (int i = 0; i < MAX_TRANSACTION; i++) ;

    }

    public void getAndBroadcastReward(double amount) {
    }

    public void addInvestor(String investorAdress) {
    }

    public void delInvestor(String investorAdress) {
    }
}
