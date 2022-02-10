package PoS;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * header : PoW.Header -> Represent the block header.
 * blockId : int -> PoW.Block identifier.
 * transaction : List<PoW.Transaction> -> List of all the transaction encapsulated in the block.
 * nodeID : int -> Represent the miner nodeID which mined or staked the block.
 */
public class Block {

    private final Header header;
    private final int blockId;
    private final List<Transaction> transactions;
    private int nodeID;
    private String nodeAddress;


    /**
     * Constructor PoW.Block
     *
     * @param blockPrev   Last PoW.Block in the blockchain needed in order to get the hash.
     * @param transaction List of transaction to be added into a new block.
     */
    public Block(Block blockPrev, List<Transaction> transaction) {
        this.transactions = new ArrayList<>(transaction);
        String trs = this.toStringAllTransaction();
        String blockTransHash = HashUtil.SHA256(trs);
        header = new Header(blockPrev, blockTransHash);
        blockId = blockPrev.blockId + 1;
    }

    /**
     * Constructor PoW.Block
     * No parameters, used in order to create the genesis block
     */
    public Block() {
        header = new Header();
        this.transactions = new ArrayList<>();
        blockId = 0;
    }

    /**
     * Getter of header
     *
     * @return header
     */
    public Header getHeader() {
        return header;
    }

    /**
     * Getter of blockId
     *
     * @return blockId
     */
    public int getBlockId() {
        return blockId;
    }

    /**
     * Getter of nodeId
     *
     * @return nodeId
     */
    public int getNodeID() {
        return nodeID;
    }


    /**
     * Getter of nodeAdress
     * @return nodeAddress
     */
    public String getNodeAddress() {
        return nodeAddress;
    }

    /**
     * Setter nodeId
     *
     * @param nodeAddress node's address
     */
    public void setNodeAddress(String nodeAddress) {
        this.nodeAddress = nodeAddress;
    }

    /**
     * Getter transaction
     *
     * @return transaction
     */
    public List<Transaction> getTransaction() {
        return transactions;
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

    /**
     * Transform into string all transaction's information
     *
     * @return All transaction's information into a string
     */
    public String toStringAllTransaction() {
        StringBuilder trs = new StringBuilder();
        for (Transaction transaction : transactions) {
            trs.append(transaction.toString());
        }
        return trs.toString();
    }

    /**
     * Function which print all transaction's information
     */
    public void printTransactions() {
        for (Transaction transaction : transactions) {
            System.out.print(transaction.toString());
        }
    }

}
