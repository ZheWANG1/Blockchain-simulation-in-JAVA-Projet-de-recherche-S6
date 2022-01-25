import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Block {
    /*
     * header : Header -> Represent the block header.
     * blockId : int -> Block identifier.
     * transaction : List<Transaction> -> List of all the transaction encapsulated in the block.
     * nodeID : int -> Represent the miner nodeID which mined or staked the block.
     */

    private final Header header;
    private final int blockId;
    private final List<Transaction> transactions;
    private int nodeID;



    public Block(Block blockPrev, List<Transaction> transaction) {
        /*
          Constructor Block
          blockPrev -> Last Block in the blockchain needed in order to get the hash.
          transaction -> List of transaction to be added into a new block.
         */
        header = new Header(blockPrev);
        this.transactions = new ArrayList<>(transaction);
        blockId = blockPrev.blockId + 1;
        //this.printTransactions();
    }

    public Block() {
        /*
            Constructor Block
            No parameters, used in order to create the genesis block
         */
        header = new Header();
        this.transactions = new ArrayList<>();
        blockId = 0;
    }

    //Getter
    public Header getHeader() {
        /*
            Getter of header
         */
        return header;
    }
    public int getBlockId() {
        /*
            Getter of blockId
         */

        return blockId;
    }
    public int getNodeID(){
        /*
            Getter of nodeId
         */
        return nodeID;
    }
    public List<Transaction> getTransaction() {
        /*
            Getter of transaction
         */
        return transactions;
    }

    // Setter
    public void setNodeID(int Id){
        /*
            Setter nodeId
         */
        nodeID = Id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return header.equals(block.header);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header);
    }

    @Override
    public String toString() {
        return header.toString();
    }
    public String toStringAllTransaction() {
        /*
            ToString Function,
            render all the Transaction into one String
         */
        StringBuilder trs = new StringBuilder();
        for (Transaction transaction : transactions) {
            trs.append(transaction.toString());
        }
        return trs.toString();
    }
    public void printTransactions() {
        /*
            Print all transaction's information
         */
        for (Transaction transaction : transactions) {
            System.out.print(transaction.toString());
        }
    }

}
