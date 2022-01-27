import java.util.Objects;

/**
 *  Class Header timeStamp : long -> Date of creation of the block
 *  headerHashPrev : String -> Hash of the last block's header
 *  blockTransHash : String -> Hash of all transaction's information
 *  headerHash : String -> Hash of the header containing all block's information
 *  nonce : int -> Nonce found by a Miner/Validator ?
 */
public class Header {

    private final long timeStamp;
    protected final String headerHashPrev;
    protected final String blockTransHash;
    protected String headerHash;
    private int nonce;

    /**
     * Constructor Header
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
     * Constructor Header
     * @param blockPrev -> Previous block
     */
    public Header(Block blockPrev, String blockTransHash) {
        timeStamp = System.currentTimeMillis(); // Get the current date
        Header h = blockPrev.getHeader(); // Get previous block's header
        headerHashPrev = h.getHeaderHash(); // Get header's hash
    }

    /**
     *
     * @param hHp
     * @param hH
     * @param trsHash
     */
    public Header(String hHp, String hH, String trsHash) {
        timeStamp = 0;
        headerHashPrev = hHp;
        headerHash = hH;
        blockTransHash = trsHash;
    }

    public String calcHeaderHash(int nonce) {
        /*
            This function return the header's hash
            nonce : int -> Nonce found by Miner/Validator
         */
        this.nonce = nonce;
        String concat = headerHashPrev + timeStamp + nonce + blockTransHash; // Whole block's information
        return HashUtil.SHA256(concat); // Return the hash of the whole block's information
    }

    // Setter
    public void setHeaderHash(String headerHash) {
        /*
            Setter headerHash
         */
        this.headerHash = headerHash;
    }

    public void setNonce(int nonce) {
        /*
            Setter nonce
         */
        this.nonce = nonce;
    }

    // Getter
    public long getTimeStamp() {
        /*
            Getter timeStamp
         */
        return timeStamp;
    }

    public int getNonce() {
        /*
            Getter nonce
         */
        return nonce;
    }

    public String getPrevHash() {
        /*
            Getter prevHash
         */
        return headerHashPrev;
    }

    public String getBlockTransHash() {
        /*
            Getter blockTransHash
         */
        return blockTransHash;
    }

    public String getHeaderHash() {
        /*
            Getter headerHash
         */
        return headerHash;
    }

    // Others
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
