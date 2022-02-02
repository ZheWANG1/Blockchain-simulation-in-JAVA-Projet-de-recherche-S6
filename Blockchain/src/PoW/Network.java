package PoW;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Network {
    private final List<Node> network = new ArrayList<>();
    private final Map<Integer, PublicKey> keyTable = new HashMap<>();
    private final int INIT_DIFFICULTY = 4;
    private int difficulty = INIT_DIFFICULTY;

    public Network() {
    }

    public int getDifficulty() {
        return difficulty;
    }

    public List<Node> getNetwork() {
        return network;
    }

    public PublicKey getPkWithID(int id) {
        return keyTable.get(id);
    }

    public void addNode(Node node) {
        network.add(node);
        difficulty = network.size() / 20 + INIT_DIFFICULTY;
        try {
            keyTable.put(node.getNodeId(), node.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void broadcastTransaction(Transaction transaction) {
        for (Node node : network) {
            //Pow
            if (node instanceof Miner) {
                ((Miner) node).receiptTransaction(transaction);
            }
        }
    }

    /**
     * Function updating client wallet with matching ID in the PoW.Block
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
            int toID = transaction.getToID();
            updateWalletWithID(amount, toID);
            updateWalletWithID(-(amount + takenFromTrans), transaction.getFromID());
        }
        updateWalletWithID(totalFee, b.getNodeID());
    }

    public void broadcastBlock(Block b, String signature, int nodeID, Blockchain blk) {
        b.printTransactions();
        for (Node node : network) {
            node.receiptBlock(b, signature, nodeID, blk);
        }
        System.out.println("Block " + b.getBlockId() + " found by " + nodeID + b.getHeader());
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
        if (associatedLightNode instanceof LightNode)
            ((LightNode) associatedLightNode).receiptCoin(amount);
        if (associatedLightNode instanceof FullNode)
            ((Miner) associatedLightNode).getLn().receiptCoin(amount);
    }

    public Blockchain copyBlockchainFromFN() {
        for (Node node : network) {
            if (node instanceof FullNode)
                return node.getBlockchain();
        }
        return null;
    }

    public void askAnyRequest() {
        for (Node ln : network) {
            if (ln instanceof LightNode lightNode) {
                lightNode.checkIfAllTransSent(lightNode.getLastBlock());
            }
        }
    }

    public void printWallets() {
        for (var node : network) {
            if (node instanceof LightNode) {
                System.out.println("Nom client : " + node.name + " Wallet : " + ((LightNode) node).getWallet());
            }
        }
    }
}
