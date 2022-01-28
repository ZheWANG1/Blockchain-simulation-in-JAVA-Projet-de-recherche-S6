package PoS;
/**
 * Class LightBlock
 * Block's lighter version : Has only the hash without the transaction in the block
 */
public class LightBlock extends Header {
    /**
     * Constructor LightBlock
     * @param hHp -> Previous header's hash
     * @param hH -> Current header's hash
     * @param trsHash -> Current transaction's hash
     */
    public LightBlock(String hHp, String hH, String trsHash) {
        super(hHp, hH, trsHash);
    }
}
