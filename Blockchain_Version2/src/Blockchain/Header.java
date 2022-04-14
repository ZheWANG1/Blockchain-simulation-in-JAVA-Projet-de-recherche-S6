package Blockchain;

import java.util.Objects;

/**
 * Class Header timeStamp : long -> Date of creation of the block
 * headerHashPrev : String -> Hash of the last block's header
 * blockTransHash : String -> Hash of all transaction's information
 * headerHash : String -> Hash of the header containing all block's information
 * nonce : int -> Nonce found by a Miner/Validator ?
 */
public class Header {

    protected final String headerHashPrev;
    protected final String PrevIDHash;
    private final long timeStamp;
    private int nonce;

    /**
     * Constructor Header
     * Used for the Genesis block's header
     */
    public Header() {
        timeStamp = System.currentTimeMillis();
        headerHashPrev = "";
        PrevIDHash = "";
        nonce = 0;
    }

    /**
     * Constructor Header
     */

    public Header(String hashBlockPrev, String hashBlockIdPrev) {
        timeStamp = System.currentTimeMillis(); // Get the current date
        headerHashPrev = hashBlockPrev; // Get header's hash
        PrevIDHash = Objects.requireNonNullElse(hashBlockIdPrev, "");
    }

    /**
     * Getter previous hash
     *
     * @return previous hash
     */
    public String getPrevHash() {
        return headerHashPrev;
    }

    /**
     * toString function
     *
     * @return string
     */

    public String getPrevIDHash() {
        return PrevIDHash;
    }

    public long getTimeStamp(){return this.timeStamp;}

    public String toString() {
        return "\nTS : " + timeStamp + "\nPrevBlockHash :" + this.headerHashPrev
                + "\nPrevIDBlockHash : " + this.PrevIDHash + "\nNonce : " + nonce;
    }
}
