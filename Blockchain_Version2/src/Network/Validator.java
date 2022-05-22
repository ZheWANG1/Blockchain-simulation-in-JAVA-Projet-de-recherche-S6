package Network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Class Validator
 * transactionBuffer : List<Transaction> -> A list of transactions which are waiting processing
 * nbMax : int -> Amount maximum of transactions in a block
 * validator : LightNode -> the light node which is elected as a validator
 * transactionTempo : Transaction -> Currently a received transaction
 * lock : concurrent.locks.Lock -> Technique for implement concurrent program
 * receiptTrans : boolean -> a transaction is received
 * condition : concurrent.locks.condition -> Technique for implement concurrent program
 */
public class Validator implements Runnable {
    private final static int SLOTS_MAX = 10;
    private final Lock lock = new ReentrantLock();
    private final Network network;
    private ValidatorNode validator = null;
    private int incremBlock = 0;
    private int testIncrement = 0;
    public Validator(Network network) {
        this.network = network;
    }

    /**
     * Function which choose a validator in order to guess a new block
     */
    public void chooseValidator(String ID) {
        System.out.println("Choosing a validator for a block of type " + ID);
        List<Node> listNode = network.getNetwork(); // List of nodes in the network
        Map<ValidatorNode, Double> mapProba = new HashMap<>();
        for (Node node : listNode) { // For each node in the network
            if (node instanceof ValidatorNode) { // If found node is an LightNode
                double stakeAmount;
                if (ID.equals(network.TYPE1)) {
                    stakeAmount = ((ValidatorNode) node).getStakeAmount1(); // Get LightNode's stakeAmount
                } else {
                    stakeAmount = ((ValidatorNode) node).getStakeAmount2(); // Get LightNode's stakeAmount
                }
                double stakeTime = System.currentTimeMillis() - ((ValidatorNode) node).getStakeTime(); // Get LightNode's stakeTime (How long the node have been Staking)
                mapProba.put((ValidatorNode) node, stakeAmount * (stakeTime));
            }
        }
        double sum = mapProba.values().stream().mapToDouble(v -> v).sum();
        int number_of_slots = 0;
        for (Node node : listNode) {
            if (node instanceof ValidatorNode) {
                number_of_slots += (mapProba.get(node) / sum) * 10;
            }
        }
        System.out.println("Slots : " + number_of_slots);
        int node_slots;
        List<ValidatorNode> validatorNodesSlots = new ArrayList<>(number_of_slots);
        for (int j = 0; j < number_of_slots; j++)
            validatorNodesSlots.add(null);
        for (Node node : listNode) {
            if (node instanceof ValidatorNode) {
                node_slots = (int) ((mapProba.get(node) / sum) * 10);
                int slot_index;
                for (int i = 0; i < node_slots; i++) {
                    do {
                        slot_index = (int) (Math.random() * number_of_slots);
                    }
                    while (validatorNodesSlots.get(slot_index) != null);
                    validatorNodesSlots.set(slot_index, (ValidatorNode) node);
                }
            }
        }
        System.out.print("[");
        for (ValidatorNode ln : validatorNodesSlots) {
            System.out.print(ln.name + " ");
        }
        System.out.print("]\n");

        if (sum == 0) // if anyone didn't deposit bitcoin as stake
            return;

        int chosen_node_index = (int) (Math.random() * number_of_slots);
        validator = validatorNodesSlots.get(chosen_node_index);
        System.out.println(validator.name + " is chosen");
    }

    public void validate() {
        int currentIDChosen = 0;
        boolean interrupt = false;
        while (!interrupt) {
            lock.lock();
            try {
                if (validator != null) {
                    if (currentIDChosen == 1)
                        validator.forgeBlock(this.network.TYPE1);
                    if (currentIDChosen == 2)
                        validator.forgeBlock(this.network.TYPE2);
                    validator = null;

                }
                long start = System.currentTimeMillis();
                while (true) {
                    long end = System.currentTimeMillis();
                    if (end - start > 10000) {
                        break;
                    }
                }

                HashMap<String, Integer> nbTransParType = (HashMap<String, Integer>) network.getNbTransParType();
                int nbSum = (int) nbTransParType.values().stream().collect(Collectors.summarizingInt(Integer::intValue)).getSum();
                double proba = (double) nbTransParType.get(network.TYPE1) / nbSum;
                network.T1.add(nbTransParType.get(network.TYPE1));
                network.T2.add(nbTransParType.get(network.TYPE2));
                System.out.println("Transactions : " +  "["+nbTransParType.get(network.TYPE1)+","+nbTransParType.get(network.TYPE2)+"]");
                if (proba > 0.95) proba = 0.95;
                if (proba < 0.05) proba = 0.05;
                currentIDChosen = (Math.random() < proba ? 1 : 2);
                network.PT1.add(proba*100);
                network.PT2.add(100-proba*100);

                System.out.println("T1-Probability is  " + proba*100 + " %");
                System.out.println("T2-Probability is " + (100-proba*100) + " %");
                //MOD

                // T
                if (currentIDChosen == 1) {
                    network.ELECTED.add(1);
                    chooseValidator(network.TYPE1);
                } else {
                    network.ELECTED.add(2);
                    chooseValidator(network.TYPE2);
                }
            } catch (Exception e) {
                e.printStackTrace();
                interrupt = true;
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public void run() {
        validate();
    }
}
