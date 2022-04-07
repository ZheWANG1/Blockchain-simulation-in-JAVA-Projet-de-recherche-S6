package Blockchain;

import MessageTypes.Transaction;
import Utils.HashUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * header : Header -> Represent the block header.
 * blockId : int -> Block identifier.
 * transaction : List<Transaction> -> List of all the transaction encapsulated in the block.
 * nodeID : int -> Represent the miner nodeID which mined or staked the block.
 * nodeAddress : String -> Represent the miner address which mined or staked the block.
 */
public class Block {

    private final Header header;
    private final Footer footer;
    private final String blockID;
    private final List<Transaction> transactions;
    private int nodeID;
    private String nodeAddress;

    /**
     * Constructor Block
     *
     * @param blockPrev   Last Block in the blockchain needed in order to get the hash.
     * @param transaction List of transaction to be added into a new block.
     */
    public Block(Block blockPrev, Block blockIDPrev, List<Transaction> transaction, String blockID) {
        this.transactions = new ArrayList<>(transaction);
        String trs = this.toStringAllTransaction();
        header = new Header(blockPrev.getFooter().getHash(), blockIDPrev.getFooter().getHash());
        footer = new Footer();
        footer.setHash(HashUtil.SHA256(trs + header.PrevIDHash + header.headerHashPrev));
        this.blockID = blockID;
        //System.out.println(this);
    }

    /**
     * Constructor Block
     * No parameters, used in order to create the genesis block
     */
    public Block(String blockID) {
        header = new Header();
        footer = new Footer();
        footer.setHash(HashUtil.SHA256("Master")); // First block has Hash(Master)
        this.transactions = new ArrayList<>();
        this.blockID = blockID;
        //System.out.println(this);
    }

    public Block(Block firstBlock, String ID) {
        this.transactions = new ArrayList<>();
        header = new Header(firstBlock.getFooter().getHash(), "");
        footer = new Footer();
        footer.setHash(HashUtil.SHA256("Master" + header.headerHashPrev));
        this.blockID = ID;
        //System.out.println(this);
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
    public String getBlockID() {
        return blockID;
    }

    /**
     * Getter of footer
     *
     * @return footer
     */
    public Footer getFooter() {
        return footer;
    }


    /**
     * Getter of nodeId
     *
     * @return nodeId
     */
    public int getNodeID() {
        return nodeID;
    }

    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    /**
     * Getter of nodeAdress
     *
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
        return header.equals(block.header) && footer.equals(block.footer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header);
    }

    @Override
    public String toString() {
        return "\nID : " + blockID + header.toString() + footer.toString();
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
            System.out.println(transaction.getTransactionHash());
        }
    }

}
