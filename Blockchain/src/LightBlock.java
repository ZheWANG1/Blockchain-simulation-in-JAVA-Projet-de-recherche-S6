
public class LightBlock {
    private final String headerHashPrev;
    private String headerHash;
    
    public LightBlock(String hHp, String hH) {
    	headerHashPrev = hHp;
    	headerHash = hH;
    }
}
