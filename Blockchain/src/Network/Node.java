package Network;

import Blockchain.Block;
import Blockchain.Blockchain;
import MessageTypes.Message;
import MessageTypes.Transaction;
import Utils.HashUtil;
import Utils.RsaUtil;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

/**
 * The node class, corresponding to each user on the network, includes mining nodes, trading nodes, server nodes, etc.
 * o : Object
 * cpt : int
 * nodeId : int -> Node's identifier
 * name : String -> Node's name
 * network : Network -> Node's network
 * blockchain : Blockchain -> Node's blockchain (Can be LightBlockChain if Node is a LightNode)
 * publicKey : PublicKey -> Second Node's identifier, used to verify signatures
 * privateKey : PrivateKey -> Node's Private Key act his password, needed to sign transaction
 * keys : KeyPair -> Node's public and private key
 */
public abstract class Node {
    private static final Object o = new Object();
    private static int cpt = 0;
    protected final int nodeID;
    public String name;
    protected Network network;
    protected Blockchain blockchain;
    protected PublicKey publicKey;
    protected PrivateKey privateKey;
    protected KeyPair keys;
    protected String nodeAddress;

    public Node(String name, Network network) {
        synchronized (o) {
            this.nodeID = cpt++;
        }
        blockchain = new Blockchain();
        this.name = name;
        this.network = network;
        try {
            keys = RsaUtil.generateKeyPair();
            publicKey = keys.getPublic();
            privateKey = keys.getPrivate();
            nodeAddress = HashUtil.SHA256(String.valueOf(publicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        network.addNode(this);
    }

    /**
     * All nodes should have the ability to receive blocks when a new block is discovered.
     *
     * @param b           The new block
     * @param signature   Signature of the miner who found the new block
     * @param nodeAddress Miner's id
     * @param blk         The blockchain that this miner has caught
     */
    public abstract void receiptBlock(Block b, String signature, String nodeAddress, Blockchain blk);

    //public abstract void receiptMessage(Message m);

    public void receiptMessage(Message m) {
        // If message is a transaction
        int messageType = m.getType();
        List<Object> listOfContent = m.getMessageContent();
        if (messageType == 0) {
            //System.out.println(listOfContent.get(0));
            Transaction tr = (Transaction) (listOfContent.get(0));
            receiptTransaction(tr);
        }
        // If message is a block
        if (messageType == 1) {
            Block content = (Block) listOfContent.get(0);
            Blockchain blk = (Blockchain) listOfContent.get(1);
            String nodeAddress = m.getFromAddress();
            String signature = m.getSignature();
            receiptBlock(content, signature, nodeAddress, blk);
        }
    }

    /**
     * Nodes receive transactions from other trading nodes
     *
     * @param tr A transaction
     */
    public void receiptTransaction(Transaction tr) {
    }

    public String getNodeAddress() {
        return nodeAddress;
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public int getNodeID() {
        return nodeID;
    }
}
