package PoW;

import java.util.Objects;

/**
 *  Class PoW.Header timeStamp : long -> Date of creation of the block
 *  headerHashPrev : String -> Hash of the last block's header
 *  blockTransHash : String -> Hash of all transaction's information
 *  headerHash : String -> Hash of the header containing all block's information
 *  nonce : int -> Nonce found by a PoW.Miner/PoW.Validator ?
 */
public class Header {

    private final long timeStamp;
    protected final String headerHashPrev;
    protected final String blockTransHash;
    protected String headerHash;
    private int nonce;

    /**
     * Constructor PoW.Header
     * Used for the Genesis block's header
     */
    public Header() {
        timeStamp = System.currentTimeMillis();
        blockTransHash = "";
        headerHashPrev = "";
        nonce = 0;
        headerHash = calcHeaderHash(nonce);
    }

    /**
     * Constructor PoW.Header
     * @param blockPrev -> Previous blockchain's block
     * @param blockTransHash -> Current block's transaction hash
     */
    public Header(Block blockPrev, String blockTransHash) {
        this.blockTransHash = blockTransHash;
        timeStamp = System.currentTimeMillis(); // Get the current date
        Header h = blockPrev.getHeader(); // Get previous block's header
        headerHashPrev = h.getHeaderHash(); // Get header's hash
    }

    /**
     * Constructor PoW.Header
     * @param hHp -> Previous header's hash
     * @param hH -> Current header's hash
     * @param trsHash -> Current transaction's hash
     */
    public Header(String hHp, String hH, String trsHash) {
        timeStamp = 0;
        headerHashPrev = hHp;
        headerHash = hH;
        blockTransHash = trsHash;
    }

    /**
     * Function which calculate the header's hash
     * @param nonce -> Random string tested by a miner
     * @return block's informations hash
     */
    public String calcHeaderHash(int nonce) {
        this.nonce = nonce;
        String concat = headerHashPrev + timeStamp + nonce + blockTransHash; // Whole block's information
        return HashUtil.SHA256(concat); // Return the hash of the whole block's information
    }

    // Setter

    /**
     * Setter headerHash
     * @param headerHash
     */
    public void setHeaderHash(String headerHash) {
        /*
            Setter headerHash
         */
        this.headerHash = headerHash;
    }

    /**
     * Setter nonce
     * @param nonce
     */
    public void setNonce(int nonce) {
        /*
            Setter nonce
         */
        this.nonce = nonce;
    }

    // Getter

    /**
     * Getter timeStamp
     * @return
     */
    public long getTimeStamp() {
        /*
            Getter timeStamp
         */
        return timeStamp;
    }

    /**
     * Getter nonce
     * @return
     */
    public int getNonce() {
        /*
            Getter nonce
         */
        return nonce;
    }

    /**
     * Getter previous hash
     * @return
     */
    public String getPrevHash() {
        /*
            Getter prevHash
         */
        return headerHashPrev;
    }

    /**
     * Getter blockTransHash
     * @return
     */
    public String getBlockTransHash() {
        /*
            Getter blockTransHash
         */
        return blockTransHash;
    }

    /**
     * Getter headerHash
     * @return headerHash
     */
    public String getHeaderHash() {
        /*
            Getter headerHash
         */
        return headerHash;
    }

    // Others

    /**
     * toString function
     * @return
     */
    public String toString() {
        return "\nTS : " + timeStamp + "\nprevHash : " + headerHashPrev + "\nhash : " + headerHash + "\nNonce : " + nonce;
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
