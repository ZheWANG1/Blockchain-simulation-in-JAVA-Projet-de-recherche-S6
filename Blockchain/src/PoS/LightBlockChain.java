package PoS;

import java.util.ArrayList;

/**
 * Class LightBlockChain
 * lightBlkchain : ArrayList<LightBlock> -> List of all Light version of Block
 */
public class LightBlockChain extends Blockchain {
    private final ArrayList<LightBlock> LightBlkchain = new ArrayList<>();

    public LightBlockChain() {
        super();
    }

    /**
     * Function which add a new light block to this light blockchain
     *
     * @param h Header of the new block
     */
    public void addLightHeader(Header h) {
        String previousHeaderHash = h.getPrevHash();
        String headerHash = h.getHeaderHash();
        String trsHash = h.getBlockTransHash();
        LightBlock ln = new LightBlock(previousHeaderHash, headerHash, trsHash);
        LightBlkchain.add(ln);
        while (LightBlkchain.size() >= 11) {
            LightBlkchain.remove(0);
        }
    }

}
