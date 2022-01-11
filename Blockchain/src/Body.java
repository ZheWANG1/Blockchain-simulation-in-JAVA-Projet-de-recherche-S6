public class Body {
    private String transaction;

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
