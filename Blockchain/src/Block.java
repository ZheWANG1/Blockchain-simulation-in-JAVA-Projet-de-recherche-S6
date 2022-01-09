

public class Block {

    private final Header header;
    private final Body body;
    private int blockId = 0;

    public Block(Block blockPrev) {
        header = new Header();
        body = new Body();
        if (blockPrev == null) {
        	blockId = blockPrev.blockId + 1
        }
    }

    public Header getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }
}
