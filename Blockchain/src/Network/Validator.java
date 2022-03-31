package Network;

import MessageTypes.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.random.*;

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
                if (ID.equals("1")) {
                    stakeAmount = ((ValidatorNode) node).getStakeAmount1(); // Get LightNode's stakeAmount
                }else{
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
        int currentIDChoosen = 0;
        boolean interrupt = false;
        while (!interrupt) {
            lock.lock();
            try {
                if (validator != null) {
                   validator.forgeBlock(Integer.toString(currentIDChoosen));
                   validator = null;

                }
                long start = System.currentTimeMillis();
                while(true){
                    long end = System.currentTimeMillis();
                    if (end-start > 10000){
                        break;
                    }
                }
                currentIDChoosen = (int) (Math.random() * 2) + 1;
                chooseValidator(Integer.toString(currentIDChoosen));
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
