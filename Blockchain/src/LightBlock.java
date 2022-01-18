
public class LightBlock {
    private final String headerHashPrev;
    private final String headerHash;
    private final String trsHash;
    
    public LightBlock(String hHp, String hH, String trsHash) {
    	headerHashPrev = hHp;
    	headerHash = hH;
    	this.trsHash = trsHash;
    }
    
    public String getHeaderHash() {
    	return headerHash;
    }
    
    public String getHeaderHashPrev() {
    	return headerHash;
    }
}
