package PoW;

import java.security.PublicKey;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A node class with mining capabilities and equipped with an account that can post transactions
 */
public class Miner extends Node implements Runnable {

    private final static int NB_MAX_TRANSACTIONS = 10;
    private final List<Transaction> transactionBuffer = new CopyOnWriteArrayList<>();
    private final LightNode ln;
    private final Lock lock = new ReentrantLock();
    private final Condition conditionTran = lock.newCondition();
    private final Condition conditionBlock = lock.newCondition();
    private Transaction transactionTempo;
    private int nonce = 0;
    private int difficulty;
    private boolean receiptTran = false;
    private boolean receiptBlock = false;


    public Miner(String name, Network network) {
        super(name, network, new Blockchain());
        difficulty = network.getDifficulty();
        ln = new LightNode(name, network);
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
     * Get the trading node attached to the mining node
     *
     * @return The trading node
     */
    public LightNode getLn() {
        return ln;
    }

    /**
     * Verify the signature of a transaction by public key
     *
     * @param transaction A transaction
     * @return Whether the transaction was sent by the initiator himself
     * @throws Exception Exception
     */
    public boolean verifySignature(Transaction transaction) throws Exception {
        return RsaUtil.verify("", transaction.getSignature(), network.getPkWithID(transaction.getFromID()));
    }

    /**
     * Reset operation when the node verifies that the received block is valid
     */
    private void receiptVerified() {
        nonce = 0;
        transactionBuffer.clear();
        receiptBlock = false;
    }

    /**
     * An attempt to calculate the hash value of the next block
     */
    public void mine() {
        Block block = new Block(blockchain.getLatestBlock(), transactionBuffer);
        String hash = block.getHeader().calcHeaderHash(++nonce);
        String toBeCheckedSubList = hash.substring(0, difficulty);
        if (toBeCheckedSubList.equals("0".repeat(difficulty))) {
            receiptBlock = true;
            block.getHeader().setHeaderHash(hash);
            //System.out.println(name + " " + nonce + " " + hash);
            try {
                block.setNodeID(nodeId);
                network.broadcastBlock(block, RsaUtil.sign("", this.privateKey), nodeId, blockchain.copyBlkch());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Mining nodes receive transactions from other trading nodes
     *
     * @param transaction A transaction
     */
    public void receiptTransaction(Transaction transaction) {
        lock.lock();
        try {
            transactionTempo = transaction;
            receiptTran = true;
            conditionTran.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void receiptBlock(Block b, String signature, int nodeID, Blockchain blk) {
        receiptBlock = true;
        PublicKey nodePK = network.getPkWithID(nodeID);
        try {
            difficulty = network.getDifficulty();
            if (nodeID == this.nodeId) {
                blockchain.addBlock(b);
                receiptVerified();
                new Thread(() -> network.askAnyRequest()).start();
            } else if (RsaUtil.verify("", signature, nodePK)) {
                if (!blockchain.getLatestBlock().equals(b)) {
                    if (this.blockchain.getSize() <= blk.getSize()) {
                        this.blockchain = blk.copyBlkch();
                        blockchain.addBlock(b);
                        receiptVerified();
                    }
                } else if (this.blockchain.getSize() < blk.getSize()) {
                    this.blockchain = blk.copyBlkch();
                    receiptVerified();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        new Thread(() -> {
            while (true) {
                lock.lock();
                try {
                    while (!receiptTran) {
                        conditionTran.await();
                    }
                    receiptTran = false;
                    if (verifySignature(transactionTempo) && transactionBuffer.size() < NB_MAX_TRANSACTIONS) {
                        if (!transactionBuffer.contains(transactionTempo)) {
                            transactionBuffer.add(transactionTempo);
                        }
                    }
                    transactionTempo = null;

                    conditionBlock.signalAll();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }).start();

        while (true) {
            while (!receiptBlock) {
                lock.lock();
                try {
                    while (!receiptTran && transactionBuffer.isEmpty()) {
                        conditionBlock.await();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
                mine();
            }
        }
    }
}
