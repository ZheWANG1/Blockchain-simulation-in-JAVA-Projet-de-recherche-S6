package Network;
import MessageTypes.Message;
import Blockchain.Block;
import Blockchain.Blockchain;
import MessageTypes.Transaction;
import PoS.FullNode;
import PoS.LightNode;

import PoW.Miner;

import java.security.PublicKey;
import java.util.*;

/**
 * Class network
 * This class simulate a network (Broadcast P2P)
 * network : List<Node> -> List of node in the network
 * keyTable : Map<Integer, PublicKey> -> Map table of NodeID and PublicKey in order to verify signatures.
 */
public class Network {
    private final static int INIT_DIFFICULTY = 4;
    private final static int CHANGE_DIFFICULTY = 50;
    private final List<Node> network = new ArrayList<>();
    private final Map<String, PublicKey> keyTable = new HashMap<>();
    private int difficulty = INIT_DIFFICULTY;
    public String mode = "POW";

    public Network() {
    }

    /**
     * Getter difficulty
     *
     * @return difficulty
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Getter network
     *
     * @return List of node in the network
     */
    public List<Node> getNetwork() {
        return network;
    }

    /**
     * Function which return the publicKey of a node
     *
     * @param address -> Node's address
     * @return Node's public key
     */
    public PublicKey getPkWithAddress(String address) {
        return keyTable.get(address);
    }

    /**
     * Function which add a node to the network
     *
     * @param node Node to be added
     */
    public void addNode(Node node) {
        network.add(node);
        difficulty = network.size() / CHANGE_DIFFICULTY + INIT_DIFFICULTY;
        try {
            keyTable.put(node.getNodeAddress(), node.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void broadcastMessage(Message m){
        for (Node n: network){
            n.receiptMessage(m);
        }
        if (m.getType() == 1){
            Block block;
            try {
                block = this.copyBlockchainFromFN().getUpdateBlock();
                updateAllWallet(block);
                System.out.println("--Wallet--");
                printWallets();
            }catch (NullPointerException ignored){
                ;
            }
        }
    }



    /**
     * Function updating client wallet with matching ID in the Block
     *
     * @param b The new block
     */
    private void updateAllWallet(Block b) {
        double totalFee = 0;
        List<Transaction> t = b.getTransaction();
        ValidatorNode vn = null;
        for(Node n: network){
            if( n.nodeAddress.equals(b.getNodeAddress())){
                vn = ((ValidatorNode) n);
            }
        }
        for (Transaction transaction : t) {
            transaction.confirmed();
            double takenFromTrans = (transaction.getTransactionFee()) * transaction.getAmount();
            totalFee += takenFromTrans;
            double amount = transaction.getAmount();
            String toAddress = transaction.getToAddress();
            updateWalletWithAddress(amount, toAddress);
            updateWalletWithAddress(-(amount + takenFromTrans), transaction.getFromAddress());
            if (this.mode.equals("POS")){
                Set<String> investorList = vn.getInvestorList();
                double otherNodeReward = takenFromTrans * ValidatorNode.INVEST_RATE;
                double thisNodeReward = takenFromTrans - otherNodeReward;
                vn.fullNodeAccount.receiptCoin(thisNodeReward);
                for(String s: investorList) {
                    updateWalletWithAddress(otherNodeReward, s);
                }
            }
        }

        updateWalletWithAddress(totalFee, b.getNodeAddress());
    }



    /**
     * Function updating client wallet with matching ID
     *
     * @param amount        The amount of transaction
     * @param clientAddress The beneficiary's address
     */
    public void updateWalletWithAddress(double amount, String clientAddress) {
        int i = 0;
        Node associatedLightNode = network.get(i);
        while (!associatedLightNode.getNodeAddress().equals(clientAddress)) {
            associatedLightNode = network.get(++i);
        }
        if (associatedLightNode instanceof PoW.LightNode)
            ((PoW.LightNode) associatedLightNode).receiptCoin(amount);
        if (associatedLightNode instanceof PoW.FullNode)
            ((PoW.FullNode) associatedLightNode).getLn().receiptCoin(amount);
        if (associatedLightNode instanceof PoS.LightNode)
            ((PoS.LightNode) associatedLightNode).receiptCoin(amount);
    }

    /**
     * Function which copy the current blockchain of a node
     *
     * @return network blockchain
     */
    public Blockchain copyBlockchainFromFN() {
        for (Node node : network) {
            if (node instanceof PoW.FullNode) {
                return node.getBlockchain();
            }
            if(node instanceof PoS.FullNode){
                return node.getBlockchain();
            }
        }
        throw new NullPointerException();
    }


    /**
     * Function which print all the wallet state
     */
    public void printWallets() {
        for (var node : network) {
            if (node instanceof PoS.LightNode) {
                System.out.println("Nom client : " + node.name + " Wallet : " + ((PoS.LightNode) node).getWallet());
            }
            if (node instanceof PoW.LightNode){
                System.out.println("Nom client : " + node.name + " Wallet : " + ((PoW.LightNode) node).getWallet());
            }
        }
    }
}
