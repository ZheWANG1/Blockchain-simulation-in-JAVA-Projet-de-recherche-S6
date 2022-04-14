package Blockchain;

/**
 * Class LightBlock
 * Block's lighter version : Has only the hash without the transaction in the block
 */
public class LightBlock extends Header {
    private Footer footer;

    /**
     * Constructor LightBlock
     *
     * @param hHp     Previous header's hash
     * @param hH      Current header's hash
     * @param trsHash Current transaction's hash
     */
    public LightBlock(String hHp, Footer footer, String trsHash) {
        super(hHp, trsHash);
        this.footer = footer;
    }
}
