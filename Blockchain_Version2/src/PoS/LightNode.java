package PoS;

import Blockchain.Block;
import Blockchain.Blockchain;
import Blockchain.LightBlockChain;
import MessageTypes.Message;
import MessageTypes.Transaction;
import Network.Network;
import Network.Node;
import Network.ValidatorNode;
import Utils.RsaUtil;

import java.util.HashMap;

/**
 * Class LightNode
 * TRANSACTION_FEE : double -> Transaction fee which apply when a lightNode send money to another LightNode
 * INIT_WALLET : int -> Initialize the wallet with INIT_WALLET coins
 * wallet : double -> LightNode's coin balance
 * stakeAmount : double -> LightNode's "stake amount"
 * stakeTime : double -> LightNode's time served in the validation process
 * lastBlock : Block -> LastBlock received by the lightNode
 * transactionBuffer : List<Transaction> -> List of transaction sent by the lightNode but not yet in the blockchain
 */
public class LightNode extends Node {
    private final static double TRANSACTION_FEE = 0.1;
    private final static int INIT_WALLET = 100000;
    private double wallet1;
    private double wallet2;
    private double stakeAmount1;
    private double stakeAmount2;
    private double stakeTime;
    private Block lastBlock;
    private ValidatorNode validator = null;

    /**
     * Constructor LightNode
     *
     * @param name    LightNode's name ( Client name )
     * @param network Network in which the lightNode is in
     */
    public LightNode(String name, Network network) {
        super(name, network);
        this.blockchain = new LightBlockChain(network);
        this.wallet1 = INIT_WALLET;
        this.wallet2 = INIT_WALLET;
        this.stakeAmount1 = 0;
        this.stakeAmount2 = 0;
        this.stakeTime = System.currentTimeMillis();
    }

    public double getWallet(String type) {
        if (type.equals(network.TYPE1)) {
            return wallet1;
        } else {
            return wallet2;
        }
    }

    public double getStakeAmount1() {
        return stakeAmount1;
    }

    public double getStakeAmount2() {
        return stakeAmount2;
    }

    public double getStakeTime() {
        return stakeTime;
    }

    public Block getLastBlock() {
        return lastBlock;
    }

    public ValidatorNode getValidator() {
        return validator;
    }

    /**
     * Function which change the identity (validator) of this lightnode
     *
     * @param validator instance of the validator
     */
    public void setValidator(ValidatorNode validator) {
        this.validator = validator;
    }

    /**
     * Function which send a transaction to the network in order to be added in all blockchain
     *
     * @param amount      Amount of coin to be sent
     * @param nodeAddress Address of the receiver
     */
    public void sendMoneyTo(double amount, String nodeAddress, String transactionType) {
        if (transactionType.equals(network.TYPE1)) {
            if (wallet1 - amount * (1 + TRANSACTION_FEE) < 0) {
                System.out.println(name + " Not enough currency of type " + transactionType + " to send"); // Whatever the currency
                System.out.println("Rejected transaction");
                return;
            }
        } else {
            if (wallet2 - amount * (1 + TRANSACTION_FEE) < 0) {
                System.out.println(name + " Not enough currency of type " + transactionType + " to send"); // Whatever the currency
                System.out.println("Rejected transaction");
                return;
            }
        }
        Transaction toSend = new Transaction(transactionType, this.getNodeAddress(), nodeAddress, amount, System.currentTimeMillis(), TRANSACTION_FEE, privateKey);
        Message m = null;
        try {
            m = new Message(this.nodeAddress, nodeAddress, RsaUtil.sign(toSend.toString(), privateKey), System.currentTimeMillis(), 0, toSend);
        } catch (Exception e) {
            e.printStackTrace();
        }
        network.broadcastMessage(m);

        //compter le nb de transaction par son type
        HashMap<String, Integer> nbTransParType = (HashMap<String, Integer>) network.getNbTransParType();
        network.setNbTransParType(transactionType, nbTransParType.get(transactionType) + 1);
        //System.out.println(this.name + " Broadcasted a transaction");
    }


    /**
     * Function which add or reduce a client's coins amount
     *
     * @param amount Coin's amount
     */
    public void receiptCoin(double amount, String type) {
        String order = amount < 0 ? " Lost " : " received "; // if amount < 0 than order = Lost else Received
        if (type.equals(network.TYPE1)) {
            wallet1 += amount;
        } else {
            wallet2 += amount;
        }
        //System.out.println(this.name + order + amount + " currency of type : " + type);
    }

    @Override
    public void receiptBlock(Block b, String signature, String nodeAddress, Blockchain blk) {
        lastBlock = b;
        ((LightBlockChain) this.blockchain).addLightHeader(b.getHeader(), b.getFooter());
    }

    /**
     * Function which modify the stake amount of a user
     *
     * @param amount the stake amount of a user
     */
    public void stake(int amount, String type) {
        if (type.equals(network.TYPE1)) {
            if (wallet1 < amount) {
                System.out.println(name + " don't have enough money for stake in wallet1");
            }
            stakeAmount1 = amount;
            this.wallet1 -= amount;
        } else {
            if (wallet1 < amount) {
                System.out.println(name + " don't have enough money for stake in wallet1");
            }
            stakeAmount2 = amount;
            this.wallet2 -= amount;
        }
        stakeTime = System.currentTimeMillis();
        //System.out.println(name + " deposit " + amount + " as stake");
    }
}
