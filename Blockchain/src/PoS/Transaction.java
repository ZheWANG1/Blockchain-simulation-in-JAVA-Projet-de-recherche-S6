package PoS;

import java.security.PrivateKey;

/**
 * Transaction class with transaction information stored
 */
public class Transaction {
    private static final Object o = new Object();
    private static int cpt = 0;
    private final String transactionHash;
    private final String fromAddress;
    private final String toAddress;
    private final double amount;
    private final double transactionFee;
    private final long timeStamp;
    private final int transactionID;
    private String signature;
    private boolean confirmedTrans = false;

    public Transaction(String transaction, String fromAddress, String toAddress, double amount, long timeStamp, double transactionFee, PrivateKey pv) {
        synchronized (o) {
            transactionID = cpt++;
        }
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
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

    public String getFromAddress() {
        return fromAddress;
    }

    public String getToAddress() {
        return toAddress;
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
        return "" + toAddress + " sent " + amount + "LD to " + fromAddress + " timestamp : " + timeStamp + " Transaction fee : " + transactionFee + "\n";
    }

}
