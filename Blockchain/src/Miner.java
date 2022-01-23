import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Miner extends Node implements Runnable {

    private final List<Transaction> transactionBuffer = new CopyOnWriteArrayList<>();
    private final int clientID;
    private final LightNode ln;
    private Transaction transactionTempo;
    private int nbMax = 2;
    private int nonce = 0;
    private int difficulty;
    private final Lock lock = new ReentrantLock();
    private final Condition conditionTran = lock.newCondition();
    private final Condition conditionBlock = lock.newCondition();
    private boolean receiptTran = false;
    private boolean receiptBlock = false;


    public Miner(String name, Network network) {
        super(name, network, new Blockchain());
        difficulty = network.getDifficulty();
        ln =  new LightNode(name, network);
        clientID = ln.getNodeId();
        try {
            keys = RsaUtil.generateKeyPair();
            publicKey = keys.getPublic();
            privateKey = keys.getPrivate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        network.addNode(this);
    }

    public boolean verifySignature(Transaction transaction) throws Exception {
        return RsaUtil.verify("", transaction.getSignature(), network.getPkWithID(transaction.getFromID()));
    }

    public void mine() {
        Block block = new Block(blockchain.getLatestBlock(), transactionBuffer);
        String hash = block.getHeader().calcHeaderHash(++nonce);
        String toBeCheckedSubList = hash.substring(0, difficulty);
        if (toBeCheckedSubList.equals("0".repeat(difficulty))) {
            block.getHeader().setHeaderHash(hash);
            //System.out.println(name + " " + nonce + " " + hash);
            try {
                block.setNodeID(nodeId);
                network.broadcastBlock(block, RsaUtil.sign("",this.privateKey), nodeId, blockchain);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

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

    public void receiptBlock(Block b,String signature, int nodeID, Blockchain blk) {
        //transactionBuffer.clear();
        PublicKey nodePK = network.getPkWithID(nodeID);
        try {
            if (RsaUtil.verify("", signature, nodePK)){
                if (!blockchain.getLatestBlock().equals(b)){
                    if (this.blockchain.getSize() <= blk.getSize()){
                        this.blockchain = blk.copyBlkch();
                        blockchain.addBlock(b);
                        receiptBlock = true;
                    }
                } else {
                    if (this.blockchain.getSize() < blk.getSize()){
                        this.blockchain = blk.copyBlkch();
                        receiptBlock = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LightNode getLn() {
        return ln;
    }

    /*
    public String mineBlock(String s) {
        //""" MINE THE BLOCK TILL THE FIRST N LETTERS ARE 0.""
        while (true) {
            nonce = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
            String test_hash = s + nonce;
            test_hash = HashUtil.SHA256(test_hash);
            String toBeCheckedSubList = test_hash.substring(0, nbOfZeros);
            if (toBeCheckedSubList.equals("0".repeat(nbOfZeros))) {
                return test_hash;
            }
        }
    }

 */

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
                    if (verifySignature(transactionTempo) && transactionBuffer.size() < nbMax) {
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
            transactionBuffer.clear();
            receiptBlock = false;
            new Thread(() -> {
                network.askAnyRequest();
            }).start();

        }

    }

    @Override
    public void receiptBlock(Block b) {

    }
}
