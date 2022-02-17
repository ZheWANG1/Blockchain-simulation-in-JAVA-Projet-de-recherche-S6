package PoS;

import Blockchain.Block;
import Blockchain.Blockchain;
import Blockchain.LightBlockChain;
import MessageTypes.Message;
import MessageTypes.Transaction;
import Network.Network;
import Network.Node;
import Network.Validator;

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
    private final static int INIT_WALLET = 100;
    private double wallet;
    private double stakeAmount;
    private double stakeTime;
    private Block lastBlock;
    private Validator validator = null;

    /**
     * Constructor LightNode
     *
     * @param name    LightNode's name ( Client name )
     * @param network Network in which the lightNode is in
     */
    public LightNode(String name, Network network) {
        super(name, network, new LightBlockChain());
        this.wallet = INIT_WALLET;
        this.stakeAmount = 0;
        this.stakeTime = System.currentTimeMillis();
    }

    public double getWallet() {
        return wallet;
    }

    public double getStakeAmount() {
        return stakeAmount;
    }

    public double getStakeTime() {
        return stakeTime;
    }

    public Block getLastBlock() {
        return lastBlock;
    }

    public Validator getValidator() {
        return validator;
    }

    /**
     * Function which change the identity (validator) of this lightnode
     *
     * @param validator instance of the validator
     */
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    /**
     * Function which send a transaction to the network in order to be added in all blockchain
     *
     * @param amount      Amount of coin to be sent
     * @param nodeAddress Address of the receiver
     */
    public void sendMoneyTo(double amount, String nodeAddress) {
        if (wallet < amount * (1 + TRANSACTION_FEE)) {
            System.out.println(name + " Not enough bitcoin to send"); // Whatever the currency
            System.out.println("Rejected transaction");
        } else {
            Transaction toSend = new Transaction("", this.getNodeAddress(), nodeAddress, amount, System.currentTimeMillis(), TRANSACTION_FEE, privateKey);
            Message m = new Message(this.nodeAddress, nodeAddress, System.currentTimeMillis(), 0, toSend);
            network.broadcastMessage(m);
            System.out.println(this.name + " Broadcasted a transaction");
        }
    }
    
    /**
     * Function which add or reduce a client's coins amount
     *
     * @param amount Coin's amount
     */
    public void receiptCoin(double amount) {
        String order = amount < 0 ? " Lost " : " received "; // if amount < 0 than order = Lost else Received
        wallet += amount;
        System.out.println(this.name + order + amount + " bitcoins");
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
    public void stake(int amount) {
        if (wallet < amount) {
            System.out.println(name + " don't have enough money for stake");
        }
        stakeAmount = amount;
        this.wallet -= amount;
        stakeTime = System.currentTimeMillis();
        System.out.println(name + " deposit " + amount + " as stake");
    }
}
