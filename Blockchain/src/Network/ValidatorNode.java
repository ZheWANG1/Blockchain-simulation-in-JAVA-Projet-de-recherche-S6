package Network;

import Blockchain.Block;
import Blockchain.Blockchain;
import MessageTypes.Message;
import MessageTypes.Transaction;
import PoS.LightNode;
import Utils.HashUtil;
import Utils.RsaUtil;

import java.util.*;

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
public class ValidatorNode extends PoS.FullNode {
    public static int MAX_TRANSACTION = 10;
    public static double INVEST_RATE = 0.30;
    private final ArrayList<Transaction> pendingTransaction = new ArrayList<>();
    private final ArrayList<Transaction> fraudulentTransaction = new ArrayList<>();
    public final LightNode fullNodeAccount;
    private final Map<String, Double> investorList1 = new HashMap<>();
    private final Map<String, Double> investorList2 = new HashMap<>();
    private double stakeAmount1 = 0;
    private double stakeAmount2 = 0;
    private final long stakeTime = System.currentTimeMillis();

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
            //System.out.println("Transaction" + t.getTransactionHash() + " receipt by " + this.name + " and accepted");
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
        //System.out.println("Transaction list of " + this.name + " successfully updated");

    }

    public void forgeBlock(String blockID) {
        List<Transaction> inBlockTransaction = new ArrayList<>();
        for (int i = 0; (i < MAX_TRANSACTION) && (i < pendingTransaction.size()); i++) {
            if (pendingTransaction.get(i).getTransactionID().equals(blockID))
                inBlockTransaction.add(pendingTransaction.get(i));
        }
        Block prevBlockID = this.blockchain.searchPrevBlockByID(blockID, this.blockchain.getSize()-1);
        Block forgedBlock = new Block(this.blockchain.getLatestBlock(), prevBlockID, inBlockTransaction, blockID);
        forgedBlock.setNodeID(this.nodeID);
        forgedBlock.setNodeAddress(this.nodeAddress);
        System.out.println("Block has been forged by " + this.name);
        //System.out.println("\t\t-----Block information-----");
        //forgedBlock.printTransactions();
        System.out.println("---------------------------------------------------");
        System.out.println("Broadcasting");
        //System.out.println("Broadcasting.");
        //System.out.println("Broadcasting..");
        //System.out.println("Broadcasting...");
        try {
            List<Object> messageContent = new ArrayList<>();
            messageContent.add(forgedBlock);
            messageContent.add(this.blockchain);
            Message m = new Message(this.nodeAddress, "ALL",RsaUtil.sign(HashUtil.SHA256(forgedBlock.toString()), this.privateKey), System.currentTimeMillis(), 1, messageContent);
            network.broadcastMessage(m);
            System.out.println("Block forged and broadcast successfully by " + this.name);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error signing");
        }
    }

    public void getAndBroadcastReward(double amount, String id) {
        double otherNodeReward = amount * INVEST_RATE;
        double thisNodeReward = amount - otherNodeReward;
        this.fullNodeAccount.receiptCoin(thisNodeReward, id);
        if (id.equals("1")) {
            for (String s : this.investorList1.keySet()) {
                network.updateWalletWithAddress(otherNodeReward, s, id);
            }
        } else {
            for (String s : this.investorList2.keySet()) {
                network.updateWalletWithAddress(otherNodeReward, s, id);
            }
        }
    }

    public void receiptBlock(Block b, String signature, String nodeAddress, Blockchain blk){
        updateTransactionList(b);
        try {
            if(RsaUtil.verify(b.toString(),signature, network.getPkWithAddress(nodeAddress))){
                System.out.println("Block accepted by "+ this.name);
                this.blockchain.addBlock(b);
                this.blockchain.printBlk();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addStake1(double stake){
        this.stakeAmount1 += stake;
    }
    public void addStake2(double stake) { this.stakeAmount2 += stake;}

    public void addInvestorType(String investorAddress, double stakeAmount, String type) {
        if (type.equals("1")) {
            this.investorList1.put(investorAddress, stakeAmount);
            this.stakeAmount1 += stakeAmount;
        } else{
            this.investorList2.remove(investorAddress, stakeAmount);
            this.stakeAmount2 += stakeAmount;
        }
    }

    public void delInvestor(String investorAddress, String type) {
        if (type.equals("1")) {
            this.stakeAmount1 -= investorList1.get(investorAddress);
            this.investorList1.remove(investorAddress);

        } else{
            this.stakeAmount2 -= investorList2.get(investorAddress);
            this.investorList2.remove(investorAddress);
        }
    }

    public double getStakeAmount1() {
        return this.stakeAmount1;
    }

    public double getStakeAmount2() {
        return this.stakeAmount2;
    }

    public long getStakeTime() {
        return this.stakeTime;
    }

    public Set<String> getInvestorList1(){
        return this.investorList1.keySet();
    }

    public Set<String> getInvestorList2(){
        return this.investorList2.keySet();
    }
}