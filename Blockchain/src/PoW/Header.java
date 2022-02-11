package PoW;

import java.util.Objects;

/**
 * Class Header timeStamp : long -> Date of creation of the block
 * headerHashPrev : String -> Hash of the last block's header
 * blockTransHash : String -> Hash of all transaction's information
 * headerHash : String -> Hash of the header containing all block's information
 * nonce : int -> Nonce found by a Miner/Validator ?
 */
public class Header {

    //protected final String headerHashPrev;
    protected final String blockTransHash;
    private final long timeStamp;
    protected String headerHash;
    private int nonce;

    /**
     * Constructor Header
     * Used for the Genesis block's header
     */
    public Header() {
        timeStamp = System.currentTimeMillis();
        blockTransHash = "";
        headerHash = "";
        //headerHashPrev = "";
        nonce = 0;

    }

    /**
     * Constructor Header
     *
     * @param blockPrev      Previous blockchain's block
     * @param blockTransHash Current block's transaction hash
     */
    public Header(Block blockPrev, String blockTransHash) {
        this.blockTransHash = blockTransHash;
        timeStamp = System.currentTimeMillis(); // Get the current date
        Header h = blockPrev.getHeader(); // Get previous block's header
        //headerHashPrev = h.getHeaderHash(); // Get header's hash
    }

    /**
     * Constructor Header
     *
     * @param hHp     Previous header's hash
     * @param hH      Current header's hash
     * @param trsHash Current transaction's hash
     */
    public Header(String hHp, String hH, String trsHash) {
        timeStamp = 0;
        headerHash = hH;
        //headerHashPrev = hHp;
        blockTransHash = trsHash;
    }


    // Setter
    /**
     * Function which calculate the header's hash
     *
     * @param nonce Random string tested by a miner
     * @return Block's information hash
     */
    public String calcHeaderHash(int nonce, String headerHashPrev) {
        this.nonce = nonce;
        String concat = headerHashPrev + timeStamp + nonce + blockTransHash; // Whole block's information
        return HashUtil.SHA256(concat); // Return the hash of the whole block's information
    }


    /**
     * Getter timeStamp
     *
     * @return timeStamp
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * Getter previous hash
     *
     * @return previous hash
     */
    //public String getPrevHash() {return headerHashPrev;}

    /**
     * Getter blockTransHash
     *
     * @return hash of transactions in the block
     */
    public String getBlockTransHash() {
        return blockTransHash;
    }

    /**
     * Getter headerHash
     *
     * @return headerHash
     */
    public String getHeaderHash() {
        return headerHash;
    }

    /**
     * Setter headerHash
     *
     * @param headerHash hash of header
     */
    public void setHeaderHash(String headerHash) {
        this.headerHash = headerHash;
    }

    /**
     * toString function
     *
     * @return string
     */
    public String toString() {
        // return "\nTS : " + timeStamp + "\nprevHash : " + headerHashPrev + "\nhash : " + headerHash + "\nNonce : " + nonce;
        return "\nTS : " + timeStamp  + "\nhash : " + headerHash + "\nNonce : " + nonce;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Header header = (Header) o;
        return headerHash.equals(header.headerHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headerHash);
    }
}
