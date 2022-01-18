
public class LightBlock {
    private final String headerHashPrev;
    private String headerHash;
    
    public LightBlock(String hHp, String hH) {
    	headerHashPrev = hHp;
    	headerHash = hH;
    }
    
    public String getHeaderHash() {
    	return headerHash;
    }
    
    public String getHeaderHashPrev() {
    	return headerHash;
    }
}
