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
    private final Map<String, PublicKey> keyTable = new HashMap<>();

    public Network() {
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
     * @param adress -> Node's adress
     * @return Node's public key
     */
    public PublicKey getPkWithAdress(String adress) {
        return keyTable.get(adress);
    }

    /**
     * Function which add a node to the network
     *
     * @param node Node to be added
     */
    public void addNode(Node node) {
        network.add(node);
        try {
            keyTable.put(node.getNodeAddress(), node.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Broadcast a transaction in the network
     *
     * @param transaction Transaction to be broadcast
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
        for (Node node : network) {
            new Thread(() -> node.receiptBlock(b, signature, nodeAddress, blk)).start();
        }
        System.out.println("Block " + b.getBlockId() + " found by " + nodeAddress + b.getHeader());
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
     * @param amount  The amount of transaction
     * @param address The beneficiary's adress
     */
    private void updateWalletWithAddress(double amount, String address) {
        int i = 0;
        Node associatedLightNode = network.get(i);
        while (!associatedLightNode.getNodeAddress().equals(address)) {
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
