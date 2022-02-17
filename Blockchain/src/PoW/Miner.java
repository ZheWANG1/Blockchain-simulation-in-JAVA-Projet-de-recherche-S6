package PoW;

import Blockchain.Block;
import Blockchain.Blockchain;
import MessageTypes.Transaction;
import Network.Network;
import Network.Node;
import Utils.RsaUtil;

import java.security.PublicKey;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A node class with mining capabilities and equipped with an account that can post transactions
 */
public abstract class Miner extends Node implements Runnable {

    private final static int NB_MAX_TRANSACTIONS = 10;
    private final static int TIME_TO_WAIT = 10000;
    private final LightNode ln;
    private final Lock lock = new ReentrantLock();
    private final Condition conditionTran = lock.newCondition();
    private final Condition conditionBlock = lock.newCondition();
    private final List<Transaction> transactionBuffer = new CopyOnWriteArrayList<>();
    private final List<Transaction> transactionInSize = new CopyOnWriteArrayList<>();
    private Transaction transactionTempo;
    private int nonce = 0;
    private int blockReceived = 0;
    private int transactionReceived = 0;
    private int difficulty;
    private boolean receiptTran = false;
    private boolean receiptBlock = false;
    private boolean mineWithoutTransaction = false;
    private long start, end;

    public Miner(String name, Network network) {
        super(name, network, new Blockchain());
        difficulty = network.getDifficulty();
        ln = new LightNode(name, network);
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
        return RsaUtil.verify(transaction.toString(), transaction.getSignature(), network.getPkWithAddress(transaction.getFromAddress()));
    }

    /**
     * Reset operation when the node verifies that the received block is valid
     */
    private void receiptVerified() {
        nonce = 0;
        //transactionBuffer.clear();
        receiptBlock = false;
        //receiptTran = true;
    }

    public void updateMiner(Block b) {
        List<Transaction> lt = b.getTransaction();
        //printTransactionBuffer()
        for (Transaction t : lt) {
            transactionBuffer.remove(t);
        }
        transactionInSize.clear();
        if (!transactionBuffer.isEmpty()) {
            for (Transaction t : transactionBuffer) {
                if (transactionInSize.size() < NB_MAX_TRANSACTIONS)
                    transactionInSize.add(t);
            }
        }
    }

    /**
     * An attempt to calculate the hash value of the next block
     */
    public void mine() {
        Block block = new Block(blockchain.getLatestBlock(), transactionInSize);
        block.setNodeID(this.nodeID);
        String hash = block.getHeader().calcBlockHash(++nonce, block.getHeader().getPrevHash());
        String toBeCheckedSubList = hash.substring(0, difficulty);
        if (toBeCheckedSubList.equals("0".repeat(difficulty))) {
            receiptBlock = true;
            mineWithoutTransaction = false;
            block.getHeader().setHeaderHash(hash);
            System.out.println(name + " " + nonce + " " + hash);
            try {
                block.setNodeAddress(nodeAddress);
                network.broadcastBlock(block, RsaUtil.sign(block.toString(), this.privateKey), nodeAddress, blockchain.copyBlkch());
                System.out.println("Block found by " + this.name + " and broadcast successfully");
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
        this.transactionReceived++;
        System.out.println("Transaction received by " + this.name + " = " + this.transactionReceived);
        lock.lock();
        try {
            transactionTempo = transaction;
            receiptTran = true;
            conditionTran.signalAll();
        } finally {
            lock.unlock();
        }
    }


    public void receiptBlock(Block b, String signature, String nodeAddress, Blockchain blk) {
        blockReceived++;
        System.out.println("Block received by " + this.name + " Total blocks received : " + blockReceived);
        updateMiner(b);
        receiptBlock = true;
        mineWithoutTransaction = false;
        PublicKey nodePK = network.getPkWithAddress(nodeAddress);
        try {
            difficulty = network.getDifficulty();
            if (nodeAddress.equals(this.nodeAddress)) {
                blockchain.addBlock(b);
                receiptVerified();
                System.out.println("Block received by sender");
            } else if (RsaUtil.verify(b.toString(), signature, nodePK)) {
                if (!blockchain.getLatestBlock().equals(b)) {
                    if (this.blockchain.getSize() <= blk.getSize()) {
                        this.blockchain = blk.copyBlkch();
                        blockchain.addBlock(b);
                        receiptVerified();
                        //this.printTransactionBuffer();
                    }
                } else if (this.blockchain.getSize() < blk.getSize()) {
                    this.blockchain = blk.copyBlkch();
                    receiptVerified();
                    //this.printTransactionBuffer();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printTransactionBuffer() {
        for (Transaction transaction : transactionBuffer) {
            System.out.println(transaction.toString());
        }
    }

    private void waitAndMine() {
        if (end - start > TIME_TO_WAIT) {
            start = System.currentTimeMillis();
            System.out.println(this.name + " is mining");
            mineWithoutTransaction = true;
            conditionBlock.signalAll();
        }
    }

    @Override
    public void run() {
        new Thread(() -> {
            start = System.currentTimeMillis();
            while (true) {
                end = System.currentTimeMillis();
                lock.lock();

                try {
                    while (!receiptTran) {
                        conditionTran.await();
                    }
                    if (transactionTempo != null) {
                        if (!transactionBuffer.contains(transactionTempo) && verifySignature(transactionTempo)) {
                            transactionBuffer.add(transactionTempo);
                            if (transactionBuffer.size() < NB_MAX_TRANSACTIONS) {
                                transactionInSize.add(transactionTempo);
                            }
                            transactionTempo = null;
                        }
                    } else {
                        waitAndMine();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }).start();

        boolean interrupt = false;
        while (!interrupt) {
            while (!receiptBlock) {
                //System.out.println("Waiting");
                lock.lock();
                try {
                    if (!mineWithoutTransaction)
                        while (transactionInSize.isEmpty() && !mineWithoutTransaction) {
                            //System.out.println("Waiting 2");
                            conditionBlock.await();
                        }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    interrupt = true;
                } finally {
                    lock.unlock();
                }
                mine();
            }
        }
    }
}
