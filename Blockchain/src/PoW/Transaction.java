package PoW;

import java.security.PrivateKey;

/**
 * PoW.Transaction class with transaction information stored
 */
public class Transaction {
    private static final Object o = new Object();
    private static int cpt = 0;
    private final String transactionHash;
    private final int fromID;
    private final int toID;
    private final double amount;
    private final double transactionFee;
    private final long timeStamp;
    private final int transactionID;
    private String signature;
    private boolean confirmedTrans = false;


    public Transaction(String transaction, int fromID, int toID, double amount, long timeStamp, double transactionFee, PrivateKey pv) {
        synchronized (o) {
            transactionID = cpt++;
        }
        this.fromID = fromID;
        this.toID = toID;
        this.amount = amount;
        this.timeStamp = timeStamp;
        this.transactionFee = transactionFee;
        this.transactionHash = HashUtil.SHA256(this.toString());
        try {
            this.signature = RsaUtil.sign(this.toString(), pv);
        } catch (Exception e) {
            e.printStackTrace();
            this.signature = null;
        }
    }

    public int getFromID() {
        return fromID;
    }

    public int getToID() {
        return toID;
    }

    public double getAmount() {
        return amount;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getSignature() {
        return signature;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public double getTransactionFee() {
        return transactionFee;
    }

    public synchronized boolean isConfirmedTrans() {
        return confirmedTrans;
    }

    public synchronized void confirmed() {
        confirmedTrans = true;
    }

    public String toString() {
        return "" + toID + " sent " + amount + "LD to " + fromID + " timestamp : " + timeStamp  + " Transaction fee : " + transactionFee+"\n";
    }

}
