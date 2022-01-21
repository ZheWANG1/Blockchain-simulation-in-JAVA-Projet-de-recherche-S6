import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Block {

    private final Header header;
    private final int blockId;
    private final List<Transaction> transactions;


    public Block(Block blockPrev, List<Transaction> transaction) {
        header = new Header(blockPrev);
        this.transactions = new ArrayList<>(transaction);
        blockId = blockPrev.blockId + 1;
        //this.printTransactions();
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
        for (Transaction transaction : transactions) {
            trs.append(transaction.toString());
        }
        return trs.toString();
    }

    public int getBlockId() {
        return blockId;
    }

    public void printTransactions() {
        for (Transaction transaction : transactions) {
            System.out.print(transaction.toString());
        }
    }
    
    public List<Transaction> getTransaction() {
    	return transactions;
    }
}
