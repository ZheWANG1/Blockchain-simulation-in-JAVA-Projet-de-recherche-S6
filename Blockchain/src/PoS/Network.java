package PoS;

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
    private final List<Node> network = new ArrayList<>();
    private final Map<Integer, PublicKey> keyTable = new HashMap<>();
    private final int INIT_DIFFICULTY = 4;
    private int difficulty = INIT_DIFFICULTY;

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
     * @param id -> Node's identifier
     * @return Node's public key
     */
    public PublicKey getPkWithID(int id) {
        return keyTable.get(id);
    }

    /**
     * Function which add a node to the network
     *
     * @param node -> Node to be added
     */
    public void addNode(Node node) {
        network.add(node);
        difficulty = network.size() / 20 + INIT_DIFFICULTY;
        try {
            keyTable.put(node.getNodeId(), node.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Broadcast a transaction in the network
     *
     * @param transaction -> Transaction to be broadcast
     */
    public void broadcastTransaction(Transaction transaction) {
        for (Node node : network) {
            if (node instanceof LightNode && ((LightNode) node).getValidator() != null) {
                Validator validator = ((LightNode) node).getValidator();
                validator.receiptTransaction(transaction);
            }
        }
    }

    /**
     * Function updating client wallet with matching ID in the Block
     *
     * @param b -> The new block
     */
    private void updateAllWallet(Block b) {
        double totalFee = 0;
        List<Transaction> t = b.getTransaction();
        for (Transaction transaction : t) {
            transaction.confirmed();
            double takenFromTrans = (transaction.getTransactionFee()) * transaction.getAmount();
            totalFee += takenFromTrans;
            double amount = transaction.getAmount();
            int toID = transaction.getToID();
            updateWalletWithID(amount, toID);
            updateWalletWithID(-(amount + takenFromTrans), transaction.getFromID());
        }
        updateWalletWithID(totalFee, b.getNodeID());
    }

    public void broadcastBlock(Block b, String signature, int nodeID, Blockchain blk) {
        for (Node node : network) {
            new Thread(() -> node.receiptBlock(b, signature, nodeID, blk)).start();
        }
        System.out.println("Block " + b.getBlockId() + " found by " + nodeID + b.getHeader());
        Block block = blk.getUpdateBlock();
        if (block != null) {
            updateAllWallet(block);
            System.out.println("--Wallet--");
            printWallets();
        }
    }

    /**
     * Function updating client wallet with matching ID
     *
     * @param amount   The amount of transaction
     * @param clientId The beneficiary's node ID
     */
    private void updateWalletWithID(double amount, int clientId) {
        int i = 0;
        Node associatedLightNode = network.get(i);
        while (associatedLightNode.getNodeId() != clientId) {
            i++;
            associatedLightNode = network.get(i);
        }
        if (associatedLightNode instanceof LightNode) {
            ((LightNode) associatedLightNode).receiptCoin(amount);
        }
    }

    /**
     * Function which copy the current blockchain of a node
     *
     * @return network blockchain
     */
    public Blockchain copyBlockchainFromFN() {
        for (Node node : network) {
            if (node instanceof FullNode)
                return node.getBlockchain();
        }
        return null;
    }

    /**
     * Function which listen the network for new transaction
     */
    public void askAnyRequest() {
        for (Node ln : network) {
            if (ln instanceof LightNode lightNode) {
                lightNode.checkIfAllTransSent(lightNode.getLastBlock());
            }
        }
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
