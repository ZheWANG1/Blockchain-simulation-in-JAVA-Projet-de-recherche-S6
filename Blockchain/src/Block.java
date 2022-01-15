public class Block {

    private final Header header;
    private final int blockId;
    private final String[] transactions;

    public Block(Block blockPrev, String[] transaction) {
        header = new Header(blockPrev);
        body = new Body(transactions);
        blockId = blockPrev.blockId + 1;
    }

    public Block(String[] transactions) {
        header = new Header();
        body = new Body(transactions);
        blockId = 0;
    }

    public Header getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }

    public int getBlockId() {
        return blockId;
    }

    public String toString() {
        return "Block ID : " + blockId + " " + header.toString() + "\n" + body.toString();
    }
}
