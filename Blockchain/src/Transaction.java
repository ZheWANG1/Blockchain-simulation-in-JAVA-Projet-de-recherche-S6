import java.security.*;

public class Transaction {
    private final String transaction;
    private final String transactionHash;
    private final String fromID;
    private final String toID;
    private final int amount;
    private final long timeStamp;
    private final String signature;
    
    public Body() {
        this.transaction = transaction;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String toString() {
        return "Transaction info : " + transaction + "\n";
    }

}
