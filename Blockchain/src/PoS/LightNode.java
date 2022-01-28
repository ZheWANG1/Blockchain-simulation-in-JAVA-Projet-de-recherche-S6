package PoS;

import java.security.PublicKey;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class LightNode
 * TRANSACTION_FEE : double -> Transaction fee which apply when a lightNode send money to another LightNode
 * INIT_WALLET : int -> Initialize the wallet with INIT_WALLET coins
 * wallet : double -> LightNode's coin balance
 * stakeAmount : double -> LightNode's "stake amount"
 * stakeTime : double -> LightNode's time served in the validation process
 * lastBlock : Block -> LastBlock received by the lightNode
 * transactionBuffer : List<Transaction> -> List of transaction sent by the lightNode but not yet in the blockchain
 * |Potential issue with the validity of thje 6th block and the transactionBuffer ?
 */
public class LightNode extends Node {
    private final static double TRANSACTION_FEE = 0.1;
    private final static int INIT_WALLET = 100;
    private double wallet;
    private double stakeAmount;
    private double stakeTime;
    private Block lastBlock;
    private List<Transaction> transactionBuffer = new CopyOnWriteArrayList<>();

    /**
     * Constructor LightNode
     * @param name -> LightNode's name ( Client name )
     * @param network -> Network in which the lightNode is in
     */
    public LightNode(String name, Network network) {
        super(name, network, new LightBlockChain());
        this.wallet = INIT_WALLET;
        this.stakeAmount = 0;
        this.stakeTime = 0;
        try {
            keys = RsaUtil.generateKeyPair();
            publicKey = keys.getPublic();
            privateKey = keys.getPrivate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        network.addNode(this);
    }

    /**
     * Function which send a transaction to the network in order to be added in all blockchain
     * @param amount -> Amount of coin to be sent
     * @param nodeId -> Identifier of the receiver
     */
    public void sendMoneyTo(double amount, int nodeId) {
        if (wallet < amount * (1 + TRANSACTION_FEE)) {
            System.out.println(name + " Not enough bitcoin to send"); // Whatever the currency
            System.out.println("Rejected transaction");
        } else {
            Transaction toSend = new Transaction("", this.nodeId, nodeId, amount, System.currentTimeMillis(), TRANSACTION_FEE, privateKey);
            network.broadcastTransaction(toSend);
            transactionBuffer.add(toSend);
        }
    }

    /**
     * Function which check that all the transaction sent are validated 
     * @param b -> Block to analyze (Most likely the last verified block)
     */
    public void checkIfAllTransSent(Block b) {
        List<Transaction> transNotSent = new CopyOnWriteArrayList<>();
        for (Transaction t : transactionBuffer) {
            List<Transaction> trans = b.getTransaction();
            if (!trans.contains(t) && !t.isConfirmedTrans()) {
                network.broadcastTransaction(t);
                transNotSent.add(t);
            }
        }
        transactionBuffer = transNotSent;
    }

    /**
     *  Getter wallet
     * @return Coin's amount
     */
    public double getWallet() {
        return wallet;
    }

    /**
     * Function which add or reduce a client's coins amount
     * @param amount -> Coin's amount
     */
    public void receiptCoin(double amount) {
        String order = amount < 0 ? " Lost " : " received "; // if amount < 0 than order = Lost else Received
        wallet += amount;
        System.out.println(this.name + order + amount + " bitcoins");
    }

    /**
     * Getter publicKey
     * @return Client public key
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public void receiptBlock(Block b, String signature, int nodeID, Blockchain blk) {
        lastBlock = b;
        ((LightBlockChain) this.blockchain).addLightHeader(b.getHeader());
    }

    /**
     * Getter lastBlock
     * @return client last block
     */
    public Block getLastBlock() {
        return lastBlock;
    }

    /**
     * Function which modify the stake amount of a user
     * @param amount
     */
    public void stake(int amount) {
        stakeAmount = amount;
        this.wallet -= amount;
        stakeTime = System.currentTimeMillis();
        System.out.println(name + " deposit " + amount + " as stake");
    }

    /**
     * Getter stakeAmount
     * @return Stake's amount
     */
    public double getStakeAmount() {
        return stakeAmount;
    }

    /**
     * Getter stakeTime
     * @return Stake's time
     */
    public double getStakeTime() {
        return stakeTime;
    }
}
