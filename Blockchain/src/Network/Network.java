package Network;
import MessageTypes.Message;
import Blockchain.Block;
import Blockchain.Blockchain;
import MessageTypes.Transaction;
import PoS.FullNode;
import PoS.LightNode;
import PoW.Miner;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public String mode;

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
        Block block;
        try {
            block = this.copyBlockchainFromFN().getUpdateBlock();
            System.out.println(block);
            updateAllWallet(block);
            System.out.println("--Wallet--");
            printWallets();
        }catch (NullPointerException e){
            System.out.println(":/");
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
        for (Transaction transaction : t) {
            transaction.confirmed();
            double takenFromTrans = (transaction.getTransactionFee()) * transaction.getAmount();
            totalFee += takenFromTrans;
            double amount = transaction.getAmount();
            String toAddress = transaction.getToAddress();
            updateWalletWithAddress(amount, toAddress);
            updateWalletWithAddress(-(amount + takenFromTrans), transaction.getFromAddress());
        }
        updateWalletWithAddress(totalFee, b.getNodeAddress());
    }


    public void broadcastBlock(Block b, String signature, String nodeAddress, Blockchain blk) {
        //b.printTransactions();
        for (Node node : network) {
            node.receiptBlock(b, signature, nodeAddress, blk);
        }
        //System.out.println("Block " + b.getBlockId() + " found by " + b.getNodeID() + b.getHeader());
        Block block = blk.getUpdateBlock();
        if (block != null) {
            updateAllWallet(block);
            System.out.println("--Wallet--");
            printWallets();
        }
        System.out.println("Finished");
    }

    /**
     * Function updating client wallet with matching ID
     *
     * @param amount        The amount of transaction
     * @param clientAddress The beneficiary's address
     */
    // Modify
    public void updateWalletWithAddress(double amount, String clientAddress) {
        if (this.mode.equals("POS")){
            for(Node n: this.network){
                if (n.nodeAddress.equals(clientAddress)){
                    if(n instanceof ValidatorNode){
                        ((ValidatorNode)n).getAndBroadcastReward(amount);
                        System.out.println(n.name);
                        return;
                    }
                }
            }

        }
        int i = 0;
        Node associatedLightNode = network.get(i);
        while (!associatedLightNode.getNodeAddress().equals(clientAddress)) {
            associatedLightNode = network.get(++i);
        }
        if (associatedLightNode instanceof LightNode)
            ((LightNode) associatedLightNode).receiptCoin(amount);
        if (associatedLightNode instanceof PoW.FullNode)
            ((PoW.FullNode) associatedLightNode).getLn().receiptCoin(amount);
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
        }
        throw new NullPointerException();
    }


    /**
     * Function which print all the wallet state
     */
    public void printWallets() {
        for (var node : network) {
            if (node instanceof LightNode) {
                System.out.println("Nom client : " + node.name + " Wallet : " + ((LightNode) node).getWallet());
            }
        }
    }
}
