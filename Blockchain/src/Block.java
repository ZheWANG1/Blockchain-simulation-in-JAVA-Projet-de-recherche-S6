import java.util.Objects;

public class Block {

    private final Header header;
    private final int blockId;
    private final Transaction[] transactions;


    public Block(Block blockPrev, Transaction[] transaction) {
        header = new Header(blockPrev);
        this.transactions = transaction;
        blockId = blockPrev.blockId + 1;
    }

    public Block() {
        header = new Header();
        this.transactions = new Transaction[0];
        blockId = 0;
    }

    public Header getHeader() {
        return header;
    }

    public String toStringAllTransaction() {
        StringBuilder trs = new StringBuilder();
        for (int i = 0; i < Objects.requireNonNull(transactions).length; i++) {
            trs.append(transactions[i].toString());
        }
        return trs.toString();
    }

    public int getBlockId() {
        return blockId;
    }

    public void printTransactions() {
        for (int i = 0; i < Objects.requireNonNull(transactions).length; i++) {
            System.out.print("" + transactions[i].toString());
        }
    }
}
