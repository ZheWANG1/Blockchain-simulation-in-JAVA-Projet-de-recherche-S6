package Network;

import Blockchain.Block;
import Blockchain.Blockchain;
import MessageTypes.Message;
import MessageTypes.Transaction;
import PoS.FullNode;
import PoS.LightNode;
import Utils.RsaUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static double INVEST_RATE = 0.30;
    private final ArrayList<Transaction> pendingTransaction = new ArrayList<>();
    private final ArrayList<Transaction> fraudulentTransaction = new ArrayList<>();
    private final LightNode fullNodeAccount;
    private final Map<String, Double> investorList = new HashMap<>();
    private double stakeAmount = 0;
    private long stakeTime = System.currentTimeMillis();

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

    public void receiptTransaction(Transaction t) {
        boolean transactionStatus = false;
        try {
            transactionStatus = verifyTransaction(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (transactionStatus) {
            System.out.println("Transaction" + t.getTransactionHash() + " receipt by " + this.name + " and accepted");
            pendingTransaction.add(t);
        } else {
            System.out.println("Transaction" + t.getTransactionHash() + " receipt by " + this.name + " but refused (Probably fraudulent)");
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
        System.out.println("Transaction list of " + this.name + " successfully updated");

    }

    public void forgeBlock() {
        List<Transaction> inBlockTransaction = new ArrayList<>();
        for (int i = 0; (i < MAX_TRANSACTION) && (i < pendingTransaction.size()); i++) {
            inBlockTransaction.add(pendingTransaction.get(i));
        }
        Block forgedBlock = new Block(this.blockchain.getLatestBlock(), inBlockTransaction);
        forgedBlock.setNodeID(this.nodeID);
        forgedBlock.setNodeAddress(this.nodeAddress);
        System.out.println("Block has been forged by " + this.name);
        System.out.println("\t\t-----Block information-----");
        forgedBlock.printTransactions();
        System.out.println("---------------------------------------------------");
        System.out.println("Broadcasting");
        System.out.println("Broadcasting.");
        System.out.println("Broadcasting..");
        System.out.println("Broadcasting...");
        try {
            List<Object> messageContent = new ArrayList<>();
            messageContent.add(forgedBlock);
            messageContent.add(this.blockchain);
            Message m = new Message(this.nodeAddress, "ALL",RsaUtil.sign(forgedBlock.toString(), this.privateKey), System.currentTimeMillis(), 1, messageContent);
            network.broadcastMessage(m);
            System.out.println("Block forged and broadcast successfully by " + this.name);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error signing");
        }
    }

    public void getAndBroadcastReward(double amount) {
        double otherNodeReward = amount * INVEST_RATE;
        double thisNodeReward = amount - otherNodeReward;
        this.fullNodeAccount.receiptCoin(thisNodeReward);
        for(String s: this.investorList.keySet()){
            network.updateWalletWithAddress(otherNodeReward,s);
        }
    }

    public void receiptBlock(Block b, String signature, String nodeAddress, Blockchain blk){
        updateTransactionList(b);
    }
    public void addStake(double stake){
        this.stakeAmount = stake;
    }

    public void addInvestor(String investorAddress, double stakeAmount) {
        this.investorList.put(investorAddress, stakeAmount);
        this.stakeAmount += stakeAmount;
    }

    public void delInvestor(String investorAddress, double stakeAmount) {
        this.investorList.remove(investorAddress, stakeAmount);
        this.stakeAmount -= stakeAmount;
    }

    public double getStakeAmount() {
        return this.stakeAmount;
    }

    public long getStakeTime() {
        return this.stakeTime;
    }
}