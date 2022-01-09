
public class Block {

    private final Header header;
    private final Body body;

    public Block( int Id, String transaction){
        header = new Header();
        body = new Body(Id, transaction);
    }

    public Header getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }
}
