package PoS;

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
     * @param h PoW.Header of the new block
     */
    public void addLightHeader(Header h) {
        String headerHash = h.getHeaderHash();
        String previousHeaderHash = h.getPrevHash();
        String trsHash = h.getBlockTransHash();
        LightBlock ln = new LightBlock(previousHeaderHash, headerHash, trsHash);
        LightBlkchain.add(ln);
        while (LightBlkchain.size() >= 11) {
            LightBlkchain.remove(0);
        }
    }

}
