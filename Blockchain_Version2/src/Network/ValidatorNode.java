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

 */
public class ValidatorNode extends PoS.FullNode {
    public static int MAX_TRANSACTION = 10;
    public static double INVEST_RATE = 0.80;
    public final LightNode fullNodeAccount;
    private final ArrayList<Transaction> pendingTransaction = new ArrayList<>();
    private final ArrayList<Transaction> fraudulentTransaction = new ArrayList<>();
    private final Map<String, Double> investorList1 = new HashMap<>();
    private final Map<String, Double> investorList2 = new HashMap<>();
    private final long stakeTime = System.currentTimeMillis();
    private double stakeAmount1 = 0;
    private double stakeAmount2 = 0;

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
            //System.out.println("Transaction" + t.getTransactionHash() + " receipt by " + this.name + " but refused (Probably fraudulent)");
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
        System.out.println((blockID));
        List<Transaction> inBlockTransaction = new ArrayList<>();
        for (int i = 0; (i < MAX_TRANSACTION) && (i < pendingTransaction.size()); i++) {
            if (pendingTransaction.get(i).getTransactionID().equals(blockID)) {
                inBlockTransaction.add(pendingTransaction.get(i));
                System.out.println(pendingTransaction.get(i).getTransactionID());
                network.setNbTransParType(blockID, network.getNbTransParType().get(blockID) - 1);
            }
        }
        long start = System.nanoTime();
        Block prevBlockID = this.blockchain.searchPrevBlockByID(blockID, this.blockchain.getSize() - 1);
        long end = System.nanoTime();
        network.ST.add((double)end-start); // IMPORTANT
        Block forgedBlock = new Block(this.blockchain.getLatestBlock(), prevBlockID, inBlockTransaction, blockID);
        forgedBlock.setNodeID(this.nodeID);
        forgedBlock.setNodeAddress(this.nodeAddress);
        System.out.println("Block has been forged by " + this.name);
        //System.out.println("---------------------------------------------------");
        System.out.println("Broadcasting");
        try {
            List<Object> messageContent = new ArrayList<>();
            messageContent.add(forgedBlock);
            messageContent.add(this.blockchain);
            Message m = new Message(this.nodeAddress, "ALL", RsaUtil.sign(HashUtil.SHA256(forgedBlock.toString()), this.privateKey), System.currentTimeMillis(), 1, messageContent);
            network.broadcastMessage(m);
            if (blockID.equals(network.TYPE1)){
                Network.NB_OF_BLOCK_OF_TYPE1_CREATED.add(Network.NB_OF_BLOCK_OF_TYPE1_CREATED.get(Network.NB_OF_BLOCK_OF_TYPE1_CREATED.size()-1)+1);
            }else{
                Network.NB_OF_BLOCK_OF_TYPE2_CREATED.add(Network.NB_OF_BLOCK_OF_TYPE2_CREATED.get(Network.NB_OF_BLOCK_OF_TYPE2_CREATED.size()-1)+1);
            }
            //System.out.println("Block forged and broadcast successfully by " + this.name);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error signing");
        }
    }

    public void getAndBroadcastReward(double amount, String id) {
        double otherNodeReward = amount * INVEST_RATE;
        double thisNodeReward = amount - otherNodeReward;
        this.fullNodeAccount.receiptCoin(thisNodeReward, id);
        if (id.equals(network.TYPE1)) {
            for (String s : this.investorList1.keySet()) {
                network.updateWalletWithAddress(otherNodeReward, s, id);
            }
        } else {
            for (String s : this.investorList2.keySet()) {
                network.updateWalletWithAddress(otherNodeReward, s, id);
            }
        }
    }

    public void receiptBlock(Block b, String signature, String nodeAddress, Blockchain blk) {
        updateTransactionList(b);
        try {
            if (RsaUtil.verify(HashUtil.SHA256(b.toString()), signature, network.getPkWithAddress(nodeAddress))) {
                //System.out.println("Block accepted by " + this.name);
                this.blockchain.addBlock(b);
                //this.blockchain.printBlk();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addStake1(double stake) {
        this.stakeAmount1 += stake;
    }

    public void addStake2(double stake) {
        this.stakeAmount2 += stake;
    }

    public void addInvestorType(String investorAddress, double stakeAmount, String type) {
        if (type.equals(network.TYPE1)) {
            this.investorList1.put(investorAddress, stakeAmount);
            this.stakeAmount1 += stakeAmount;
        } else {
            this.investorList2.remove(investorAddress, stakeAmount);
            this.stakeAmount2 += stakeAmount;
        }
    }

    public void delInvestor(String investorAddress, String type) {
        if (type.equals(network.TYPE1)) {
            this.stakeAmount1 -= investorList1.get(investorAddress);
            this.investorList1.remove(investorAddress);

        } else {
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

    public Set<String> getInvestorList1() {
        return this.investorList1.keySet();
    }

    public Set<String> getInvestorList2() {
        return this.investorList2.keySet();
    }
}