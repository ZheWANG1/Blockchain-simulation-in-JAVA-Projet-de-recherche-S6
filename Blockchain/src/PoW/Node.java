package PoW;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

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
abstract class Node {
    private static final Object o = new Object();
    private static int cpt = 0;
    protected final int nodeId;
    protected String name;
    protected Network network;
    protected Blockchain blockchain;
    protected PublicKey publicKey;
    protected PrivateKey privateKey;
    protected KeyPair keys;

    public Node(String name, Network network, Blockchain blk) {
        synchronized (o) {
            this.nodeId = cpt++;
        }
        blockchain = blk;
        this.name = name;
        this.network = network;
    }

    /**
     * All nodes should have the ability to receive blocks when a new block is discovered.
     *
     * @param b         The new block
     * @param signature Signature of the miner who found the new block
     * @param nodeID    PoW.Miner's id
     * @param blk       The blockchain that this miner has caught
     */
    public abstract void receiptBlock(Block b, String signature, int nodeID, Blockchain blk);

    public Integer getNodeId() {
        return nodeId;
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
