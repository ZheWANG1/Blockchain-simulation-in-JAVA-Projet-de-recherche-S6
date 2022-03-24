package Blockchain;

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
        String prevIDHash = h.getPrevIDHash();
        String previousHash = h.getPrevHash();
        String trsHash = h.getBlockTransHash();
        LightBlock ln = new LightBlock(previousHash, prevIDHash, f, trsHash);
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
