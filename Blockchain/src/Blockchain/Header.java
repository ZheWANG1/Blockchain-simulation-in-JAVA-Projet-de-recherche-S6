package Blockchain;

import Utils.HashUtil;

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
    protected final String blockTransHash;
    private final long timeStamp;
    private int nonce;

    /**
     * Constructor Header
     * Used for the Genesis block's header
     */
    public Header() {
        timeStamp = System.currentTimeMillis();
        blockTransHash = "";
        headerHashPrev = "";
        PrevIDHash = "";
        nonce = 0;
    }

    /**
     * Constructor Header
     *
     * @param blockPrev      Previous blockchain's block
     * @param blockTransHash Current block's transaction hash
     */
    public Header(Block blockPrev, Block blockIdPrev, String blockTransHash) {
        this.blockTransHash = blockTransHash;
        timeStamp = System.currentTimeMillis(); // Get the current date
        Footer f = blockPrev.getFooter(); // Get previous block's header
        headerHashPrev = f.getHash(); // Get header's hash
        PrevIDHash = blockIdPrev.getFooter().getHash();
    }

    /**
     * Constructor Header
     *
     * @param hHp     Previous header's hash
     * @param trsHash Current transaction's hash
     */
    public Header(String hHp, String trsHash) {
        timeStamp = 0;
        headerHashPrev = hHp;
        blockTransHash = trsHash;
    }


    // Setter

    /**
     * Function which calculate the header's hash
     *
     * @param nonce Random string tested by a miner
     * @return Block's information hash
     */
    public String calcBlockHash(int nonce, String headerHashPrev) {
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
    public String getPrevHash() {
        return headerHashPrev;
    }

    /**
     * Getter blockTransHash
     *
     * @return hash of transactions in the block
     */
    public String getBlockTransHash() {
        return blockTransHash;
    }

    /**
     * toString function
     *
     * @return string
     */

    public String getPrevIDHash(){return PrevIDHash;}
    public String toString() {
        // return "\nTS : " + timeStamp + "\nprevHash : " + headerHashPrev + "\nhash : " + headerHash + "\nNonce : " + nonce;
        return "\nTS : " + timeStamp + "\nNonce : " + nonce;

    }
}
