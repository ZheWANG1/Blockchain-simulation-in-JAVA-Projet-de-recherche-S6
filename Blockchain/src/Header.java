import java.util.concurrent.ThreadLocalRandom;

public class Header {

    private final int nbOfZeros = 5;
    private final long timeStamp; 
    private int nonce; 
    private final String headerHashPrev;
    private String headerHash;
    private final String blockTransHash;

    public Header(Block blockPrev) {
        timeStamp = System.currentTimeMillis();
        // GET TRANSACTION HASH
        String transaction = blockPrev.toStringAllTransaction();
        blockTransHash = HashUtil.SHA256(transaction);
        // GET PREVIOUS BLOCK HASH
        Header h = blockPrev.getHeader();
        headerHashPrev = h.getHeaderHash();
        headerHash = calcHeaderHash();
    }

    public Header() {
        timeStamp = System.currentTimeMillis();
        blockTransHash = "";
        headerHashPrev = "";
        calcHeaderHash();
    }

    public int getNonce() {
        return nonce;
    }

    public String calcHeaderHash() {
        String concat = headerHashPrev + blockTransHash;
        String minedBlock = mineBlock(concat);
        return minedBlock;
    }


    public String mineBlock(String s) {
    	//""" MINE THE BLOCK TILL THE FIRST N LETTERS ARE 0.""
        while (true) {
            nonce = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
            String test_hash = s + nonce;
            test_hash = HashUtil.SHA256(test_hash);
            String toBeCheckedSubList = test_hash.substring(0, nbOfZeros);
            if (toBeCheckedSubList.equals("0".repeat(nbOfZeros))) {
                return test_hash;
            }
        }
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
