import java.security.PublicKey;
import java.security.cert.CertPathChecker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Network {
    private final List<Node> network = new ArrayList<>();
    private final Map<Integer, PublicKey> keyTable = new HashMap<>();
    private int difficulty = 4;

    public Network() {
    }

    public boolean addNode(Node node) {
        network.add(node);
        difficulty = network.size() / 20 + 4;
        try {
            keyTable.put(node.getNodeId(), node.getPublicKey());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void broadcastTransaction(Transaction transaction) {
        for (Node node : network) {
            if (node instanceof Miner) {
                ((Miner) node).receiptTransaction(transaction);
            }
        }
    }

    // function updating client wallet with matching ID in the Block
    private void updateAllWallet(Block b) {
        double totalFee = 0;
        List<Transaction> t = b.getTransaction();
        for (Transaction transaction : t) {
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
        //System.out.println("Block " + blk.getLatestBlock().getBlockId() + blk.getLatestBlock().getHeader());
        for (Node node : network) {
            if (node instanceof Miner)
                node.receiptBlock(b, signature, nodeID, blk);
        }
        for (Node node : network) {
            if (node instanceof FullNode)
                node.receiptBlock(b, signature, nodeID, blk);
        }
        for (Node node : network) {
            if (node instanceof LightNode)
                node.receiptBlock(b, signature, nodeID, blk);
        }
        System.out.println("Block " + blk.getLatestBlock().getBlockId() + blk.getLatestBlock().getHeader());
        Block block = blk.getUpdateBlock();
        if (block != null) {
            updateAllWallet(block);
            System.out.println("--Wallet--");
            printWallets();
        }
    }

    public void printWallets() {
        for (var node : network) {
            if (node instanceof LightNode) {
                System.out.println("Nom client : " + node.name + " Wallet : " + ((LightNode) node).getWallet());
            }
        }
    }

    public PublicKey getPkWithID(int id) {
        return keyTable.get(id);
    }

    // function updating client wallet with matching ID
    private void updateWalletWithID(double amount, int clientId) {
        int i = 0;
        Node associatedLightNode = network.get(i);
        while (associatedLightNode.getNodeId() != clientId) {
            i++;
            associatedLightNode = network.get(i);
        }
        if (associatedLightNode instanceof LightNode)
            ((LightNode) associatedLightNode).receiptCoin(amount);
        if (associatedLightNode instanceof Miner)
            ((Miner) associatedLightNode).getLn().receiptCoin(amount);
    }

    public Blockchain copyBlockchainFromFN() {
        for (Node node : network) {
            if (node instanceof FullNode)
                return node.getBlockchain();
        }
        return null;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void askAnyRequest() {
        Block lastBlock = copyBlockchainFromFN().getLatestBlock();
        if (lastBlock == null)
            return;
        for (Node ln : network) {
            if (ln instanceof LightNode) {
                ((LightNode) ln).checkIfAllTransSent(lastBlock);
            }
        }
    }

    public List<Node> getNetwork() {
        return network;
    }
}
