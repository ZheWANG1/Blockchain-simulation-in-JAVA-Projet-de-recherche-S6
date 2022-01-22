import java.security.PublicKey;
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
        //difficulty = network.size() / 10 + 2;
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
        List<Transaction> t = b.getTransaction();
        for (Transaction transaction : t) {
            double amount = transaction.getAmount();
            int toID = transaction.getToID();
            updateWalletWithID(amount, toID);
            updateWalletWithID(-amount, transaction.getFromID());
        }
    }

    public void broadcastBlock(Block b) {

        System.out.println("Block " + this.copyBlockchainFromFN().getLatestBlock().getHeader());
        for (Node node : network) {
            if (node instanceof Miner)
                node.receiptBlock(b);
        }
        for (Node node : network) {
            if (node instanceof FullNode)
                node.receiptBlock(b);
        }
        for (Node node : network) {
            if (node instanceof LightNode)
                node.receiptBlock(b);
        }
        System.out.println("Block " + this.copyBlockchainFromFN().getLatestBlock().getHeader());
        updateAllWallet(b);
        System.out.println("\n--BROADCAST--");
        printWallets();
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
        ((LightNode) associatedLightNode).receiptCoin(amount);
    }

    public Blockchain copyBlockchainFromFN() {
        for (Node node : network) {
            if (node instanceof FullNode)
                return ((FullNode) node).getBlockchain();


        }
        return null;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
