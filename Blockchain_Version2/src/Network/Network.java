package Network;

import Blockchain.Block;
import Blockchain.Blockchain;
import MessageTypes.Message;
import MessageTypes.Transaction;

import java.security.PublicKey;
import java.util.*;

/**
 * Class network
 * This class simulate a network (Broadcast P2P)
 * network : List<Node> -> List of node in the network
 * keyTable : Map<Integer, PublicKey> -> Map table of NodeID and PublicKey in order to verify signatures.
 */
public class Network {
    public static int NB_OF_BLOCK_OF_TYPE1_CREATED;
    public static int NB_OF_BLOCK_OF_TYPE2_CREATED;
    public static int NB_OF_TRANSACTION_OF_TYPE_1_RECEIVED;
    public static int NB_OF_TRANSACTION_OF_TYPE_2_RECEIVED;
    public static double TIME_LAST_BLOCK_OF_TYPE1;
    public static double TIME_LAST_BLOCK_OF_TYPE2;
    private final static int INIT_DIFFICULTY = 4;
    private final static int CHANGE_DIFFICULTY = 50;
    public final String TYPE1;
    public final String TYPE2;
    private final List<Node> network = new ArrayList<>();
    private final Map<String, PublicKey> keyTable = new HashMap<>();
    public String mode = "POS";
    private int difficulty = INIT_DIFFICULTY;
    private Map<String, Integer> nbTransParType = new HashMap<String, Integer>();

    public Network(String type1, String type2) {
        TYPE1 = type1;
        TYPE2 = type2;
        nbTransParType.put(TYPE1,0);
        nbTransParType.put(TYPE2,0);
    }

    // Traditional blockchain
    public Network(String type1){
        TYPE1 = type1;
        TYPE2 = null;
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

    public Map<String, Integer> getNbTransParType() {
        return nbTransParType;
    }

    public void setNbTransParType(String type, int nb) {
        this.nbTransParType.put(type, nb);
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

    public void broadcastMessage(Message m) {
        for (Node n : network) {
            n.receiptMessage(m);
        }
        if (m.getType() == 1) {
            this.copyBlockchainFromFN().printBlk();
            Block block;
            try {
                block = this.copyBlockchainFromFN().getLatestBlock();
                if (!block.getNodeAddress().equals("Master"))
                    updateAllWallet(block);
                System.out.println("--Wallet--");
                printWallets();
            } catch (NullPointerException ignored) {

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
        if (mode.equals("POS")) {
            for (Node n : network) {
                if (n.nodeAddress.equals(b.getNodeAddress())) {
                    vn = ((ValidatorNode) n);
                }
            }
        }
        for (Transaction transaction : t) {
            transaction.confirmed();
            double takenFromTrans = (transaction.getTransactionFee()) * transaction.getAmount();
            totalFee += takenFromTrans;
            double amount = transaction.getAmount();
            String toAddress = transaction.getToAddress();
            updateWalletWithAddress(amount, toAddress, transaction.getTransactionID());
            updateWalletWithAddress(-(amount + takenFromTrans), transaction.getFromAddress(), transaction.getTransactionID());
            Set<String> investorList;
            if(vn != null){
                if (b.getBlockID().equals(TYPE1)) {
                    investorList = vn.getInvestorList1();
                } else {
                    investorList = vn.getInvestorList2();
                }

                double otherNodeReward = takenFromTrans * ValidatorNode.INVEST_RATE;
                double thisNodeReward = takenFromTrans - otherNodeReward;
                vn.fullNodeAccount.receiptCoin(thisNodeReward, transaction.getTransactionID());
                for (String s : investorList) {
                    updateWalletWithAddress(otherNodeReward, s, transaction.getTransactionID());
                }
            }

        }

        //System.out.println("Validator address : " + b.getNodeAddress());
        updateWalletWithAddress(totalFee, b.getNodeAddress(), b.getBlockID());
    }


    /**
     * Function updating client wallet with matching ID
     *
     * @param amount        The amount of transaction
     * @param clientAddress The beneficiary's address
     */
    public void updateWalletWithAddress(double amount, String clientAddress, String currency) {
        int i = 0;
        Node associatedLightNode = network.get(i);
        while (!associatedLightNode.getNodeAddress().equals(clientAddress)) {
            associatedLightNode = network.get(++i);
        }

        if (associatedLightNode instanceof PoS.LightNode)
            ((PoS.LightNode) associatedLightNode).receiptCoin(amount, currency);
    }

    /**
     * Function which copy the current blockchain of a node
     *
     * @return network blockchain
     */
    public Blockchain copyBlockchainFromFN() {
        for (Node node : network) {
            if (node instanceof PoS.FullNode) {
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
                System.out.println("Nom client : " + node.name + " \nWallet 1 : " + ((PoS.LightNode) node).getWallet(TYPE1) + "\n" + "Wallet 2 : " + ((PoS.LightNode) node).getWallet(TYPE2));
            }
        }
    }
}
