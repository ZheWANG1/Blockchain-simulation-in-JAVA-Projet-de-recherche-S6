public class Header {

    private final long timeStamp;
    private final String headerHashPrev;
    private final String blockTransHash;
    private String headerHash;
    private int nonce;

    public Header() {
        timeStamp = System.currentTimeMillis();
        blockTransHash = "";
        headerHashPrev = "";
        nonce = 0;
        headerHash = calcHeaderHash(nonce);
    }

    public Header(Block blockPrev) {
        timeStamp = System.currentTimeMillis();
        // GET TRANSACTION HASH
        String transaction = blockPrev.toStringAllTransaction();
        blockTransHash = HashUtil.SHA256(transaction);
        // GET PREVIOUS BLOCK HASH
        Header h = blockPrev.getHeader();
        headerHashPrev = h.getHeaderHash();
        //nonce = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        //headerHash = calcHeaderHash();
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public String calcHeaderHash(int nonce) {
        this.nonce = nonce;
        String concat = headerHashPrev + timeStamp + nonce + blockTransHash;
        return HashUtil.SHA256(concat);
    }

    public String getPrevHash() {
        return headerHashPrev;
    }

    public String getBlockTransHash() {
        return blockTransHash;
    }

    public String getHeaderHash() {
        return headerHash;
    }

    public String toString() {
        return "\nTS : " + timeStamp + "\nhash : " + headerHash + "\nNonce : " + nonce;
    }


}
