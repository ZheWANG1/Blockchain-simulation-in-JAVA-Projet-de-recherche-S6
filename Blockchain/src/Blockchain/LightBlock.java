package Blockchain;

/**
 * Class LightBlock
 * Block's lighter version : Has only the hash without the transaction in the block
 */
public class LightBlock extends Header {
    private final Footer footer;

    /**
     * Constructor LightBlock
     *
     */
    public LightBlock(Header h, Footer f) {
        super(h.getPrevHash(), h.getPrevIDHash());
        this.footer = f;
    }
}
