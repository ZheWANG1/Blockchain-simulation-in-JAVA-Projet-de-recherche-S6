
public class Block {

    private final Header header;
    private final Body body;

    public Block() {
        header = new Header();
        body = new Body();
    }

    public Header getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }
}
