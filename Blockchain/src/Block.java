public class Block {

    private final Header header;
    private final Body body;
    private int blockId;

    public Block(Block blockPrev,String transaction) {
        header = new Header(blockPrev);
        body = new Body(transaction);
        blockId = blockPrev.blockId + 1;
    }
    
    public Block(String transaction) {
    	header = new Header();
    	body = new Body(transaction);
    	blockId = 0;
    }

    public Header getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }

    public int getBlockId() { return blockId;}
    
    public String toString() {
    	return ""+header.toString()+"\n"+body.toString();
    }
}
