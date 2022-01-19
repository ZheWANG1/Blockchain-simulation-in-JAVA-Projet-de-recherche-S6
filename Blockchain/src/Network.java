import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Network {
    private final List<Node> network = new ArrayList<>();
    private final Map<Integer, PublicKey> keyTable = new HashMap<>();
    private int difficulty = 10;

    public Network() {
    }

    public boolean addNode(Node node) {
        network.add(node);
        difficulty = network.size() / 10 + 10;
        try {
            if (node instanceof LightNode)
                keyTable.put(node.getNodeId(), ((LightNode) node).getPublicKey());
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
        Transaction[] t = b.getTransaction();
        for (Transaction transaction : t) {
            double amount = transaction.getAmount();
            int toID = transaction.getToID();
            updateWalletWithID(amount, toID);
        }
    }

    public void broadcastBlock(Block b) {
        for (Node node : network) {
            if (node instanceof FullNode)
                ((FullNode) node).receiptBlock(b);
        }
        updateAllWallet(b);
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
        ((LightNode) associatedLightNode).receiptCoin(amount);
    }

    public Blockchain copyBlockchainFromFN() {
        int i = 0;
        Node node = network.get(i);
        while (!(node instanceof FullNode)) {
            i++;
            node = network.get(i);
        }
        return ((FullNode) node).getBlockchain();
    }

    public int getDifficulty() {
        return difficulty;
    }
}
