
public class Body {
    private int bodyId;
    private int headerId;
    private String transaction;

    public Body(int headerId, String transaction){
        this.bodyId = headerId;
        this.headerId = headerId;
        this.transaction = transaction;
    }

    public int getBodyId(){
        return bodyId;
    }

    public int getHeaderId(){
        return headerId;
    }

    public void setTransaction(String transaction){
        this.transaction = transaction;
    }

}
