import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Block {

    private final Header header;
    private final int blockId;
    private final List<Transaction> transactions;


    public Block(Block blockPrev, List<Transaction> transaction) {
        header = new Header(blockPrev);
        this.transactions = transaction;
        blockId = blockPrev.blockId + 1;
        this.printTransactions();
    }

    public Block() {
        header = new Header();
        this.transactions = new ArrayList<>();
        blockId = 0;
    }

    public Header getHeader() {
        return header;
    }

    public String toStringAllTransaction() {
        StringBuilder trs = new StringBuilder();
        for (int i = 0; i < Objects.requireNonNull(transactions).size(); i++) {
            trs.append(transactions.get(i).toString());
        }
        return trs.toString();
    }

    public int getBlockId() {
        return blockId;
    }

    public void printTransactions() {
        for (int i = 0; i < Objects.requireNonNull(transactions).size(); i++) {
            System.out.print("" + transactions.get(i).toString());
        }
    }
    
    public List<Transaction> getTransaction() {
    	return transactions;
    }
}
