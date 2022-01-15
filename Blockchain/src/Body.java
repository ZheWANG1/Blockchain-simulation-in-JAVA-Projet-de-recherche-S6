import java.security.*;

public class Body {
    private final String transaction;
    private final String transactionHash;
    private final String from;
    private final String to;
    private final int amount;
    private final long timeStamp;
    private final PublicKey keys;
    
    public Body(String transaction) {
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
