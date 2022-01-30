package PoW;

import java.security.PublicKey;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LightNode extends Node {
    private final static double TRANSACTION_FEE = 0.1;
    private final static int INIT_WALLET = 100;
    private double wallet;
    private Block lastBlock;
    private List<Transaction> transactionBuffer = new CopyOnWriteArrayList<>();

    public LightNode(String name, Network network) {
        super(name, network, new LightBlockChain());
        this.wallet = INIT_WALLET;
        try {
            keys = RsaUtil.generateKeyPair();
            publicKey = keys.getPublic();
            privateKey = keys.getPrivate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        network.addNode(this);
    }

    public void sendMoneyTo(double amount, int nodeId) {
        if (wallet < amount * (1 + TRANSACTION_FEE)) {
            System.out.println(name + " Not enough bitcoin to send"); // Whatever the currency
            System.out.println("Rejected transaction");
        } else {
            Transaction toSend = new Transaction("", this.nodeId, nodeId, amount, System.currentTimeMillis(), TRANSACTION_FEE, privateKey);
            network.broadcastTransaction(toSend);
            transactionBuffer.add(toSend);
            System.out.println(this.name + " Broadcasted a transaction");
        }
    }

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

    public double getWallet() {
        return wallet;
    }

    public void receiptCoin(double amount) {
        String order = amount < 0 ? " Lost " : " received ";

        wallet += amount;
        System.out.println(this.name + order + amount + " bitcoins");
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public void receiptBlock(Block b, String signature, int nodeID, Blockchain blk) {
        lastBlock = b;
        ((LightBlockChain) this.blockchain).addLightHeader(b.getHeader());
    }

    public Block getLastBlock() {
        return lastBlock;
    }

}
