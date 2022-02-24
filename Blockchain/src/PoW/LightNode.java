package PoW;

import Blockchain.Block;
import Blockchain.Blockchain;
import Blockchain.LightBlockChain;
import MessageTypes.Message;
import MessageTypes.Transaction;
import Network.Network;
import Network.Node;
import Utils.HashUtil;
import Utils.RsaUtil;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class LightNode
 * TRANSACTION_FEE : double -> Transaction fee which apply when a lightNode send money to another LightNode
 * INIT_WALLET : int -> Initialize the wallet with INIT_WALLET coins
 * wallet : double -> LightNode's coin balance
 * lastBlock : Block -> LastBlock received by the lightNode
 * transactionBuffer : List<Transaction> -> List of transaction sent by the lightNode but not yet in the blockchain
 */
public class LightNode extends Node {
    private final static double TRANSACTION_FEE = 0.1;
    private final static int INIT_WALLET = 100;
    private double wallet;
    private Block lastBlock;

    /**
     * Constructor LightNode
     *
     * @param name    LightNode's name ( Client name )
     * @param network Network in which the lightNode is in
     */
    public LightNode(String name, Network network) {
        super(name, network);
        this.blockchain = new LightBlockChain();
        this.wallet = INIT_WALLET;
    }

    /**
     * Function which send a transaction to the network in order to be added in all blockchain
     *
     * @param amount  Amount of coin to be sent
     * @param address address of the receiver
     */
    public void sendMoneyTo(double amount, String address) {
        if (wallet < amount * (1 + TRANSACTION_FEE)) {
            System.out.println(name + " Not enough bitcoin to send"); // Whatever the currency
            System.out.println("Rejected transaction");
        } else {
            Transaction toSend = new Transaction("", this.getNodeAddress(), address, amount, System.currentTimeMillis(), TRANSACTION_FEE, privateKey);
            Message m = null;
            try {
                m = new Message(nodeAddress, address, RsaUtil.sign(toSend.toString(), this.privateKey),System.currentTimeMillis(),0, toSend);
            } catch (Exception e) {
                e.printStackTrace();
            }
            network.broadcastMessage(m);
            System.out.println(this.name + " Broadcasted a transaction");
        }
    }
    
    public double getWallet() {
        return wallet;
    }

    /**
     * Function which add or reduce a client's coins amount
     *
     * @param amount Coin's amount
     */
    public void receiptCoin(double amount) {
        String order = amount < 0 ? " Lost " : " received ";

        wallet += amount;
        System.out.println(this.name + order + amount + " bitcoins");
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public void receiptBlock(Block b, String signature, String nodeAddress, Blockchain blk) {
        
        lastBlock = b;
        ((LightBlockChain) this.blockchain).addLightHeader(b.getHeader(), b.getFooter());
    }

    public Block getLastBlock() {
        return lastBlock;
    }


}
