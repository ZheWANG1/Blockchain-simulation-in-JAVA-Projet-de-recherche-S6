
public class Body {
    private String transaction;

    public void body(int bodyId, int headerId, String transaction){
        this.transaction = transaction;
    }


    public void setTransaction(String transaction){
        this.transaction = transaction;
    }
    
    public String getTransaction() {
    	return transaction;
    }

}
