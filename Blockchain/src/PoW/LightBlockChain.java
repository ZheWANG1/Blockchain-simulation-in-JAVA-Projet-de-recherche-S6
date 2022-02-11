package PoW;

import java.util.ArrayList;

/**
 * A light blockchain class used by transaction nodes that keeps only the ten most recent blockheads.
 */
public class LightBlockChain extends Blockchain {
    private final ArrayList<LightBlock> LightBlkchain = new ArrayList<>();

    public LightBlockChain() {
        super();
    }

    /**
     * Add new light block to this light blockchain
     *
     * @param h Header of the new block
     */
    public void addLightHeader(Header h, Footer f) {
        String headerHash = h.getHeaderHash();
        String previousHash = f.getPrevHash();
        String trsHash = h.getBlockTransHash();
        LightBlock ln = new LightBlock(previousHash, headerHash, trsHash);
        LightBlkchain.add(ln);
        while (LightBlkchain.size() >= 11) {
            LightBlkchain.remove(0);
        }
    }

}
