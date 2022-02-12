package PoS;


import PoW.LightBlock;

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
     * Add new light block to this light blockchain
     *
     * @param h Header of the new block
     */
    public void addLightHeader(Header h, Footer f) {
        String headerHash = h.getHeaderHash();
        String previousHash = f.getPrevHash();
        String trsHash = h.getBlockTransHash();
        PoW.LightBlock ln = new PoW.LightBlock(previousHash, headerHash, trsHash);
        LightBlkchain.add(ln);
        while (LightBlkchain.size() >= 11) {
            LightBlkchain.remove(0);
        }
    }

    public void printBlk() {
        for (LightBlock lb : LightBlkchain) {
            System.out.print(lb.toString());
        }
    }
}
