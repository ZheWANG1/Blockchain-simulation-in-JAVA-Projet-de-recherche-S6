public class Block {

    private final Header header;
    private final int blockId;
    private final Transaction[] transactions;
 

    public Block(Block blockPrev, Transaction[] transaction) {
        header = new Header(blockPrev);
        this.transactions = transaction;
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

    public Transaction toStringAllTransaction() {
        String trs = ""
    		for (in i = 0; i <transaction.length(); i++) {
            	trs += transaction[i].toString();
            }
    	return trs;
    }

    public int getBlockId() {
        return blockId;
    }

    public void printTransactions() {
        for (in i = 0; i <transaction.length(); i++) {
        	System.out.print(""+transaction[i].toString());
        }
    }
}
