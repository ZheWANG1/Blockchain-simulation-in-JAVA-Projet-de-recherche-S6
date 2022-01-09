
public class Block {

    private final Header header;
    private final Body body;
    private int blockId;

    public Block(Block blockPrev) {
        header = new Header(blockPrev);
        body = new Body();
        blockId = blockPrev.blockId + 1;
    }
    
    public Block() {
    	header = new Header();
    	body = new Body();
    	System.out.print(header.toString());
    	blockId = 0;
    }

    public Header getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }

    public int getBlockId() { return blockId;}
}
