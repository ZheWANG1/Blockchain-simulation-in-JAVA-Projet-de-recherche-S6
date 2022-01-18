import java.util.ArrayList;

public class LightBlockChain {
	private static int cpt = 0;
	private final int lightBlockID;
	private final ArrayList<LightBlock> LightBlkchain = new ArrayList<LightBlock>();
	
	public LightBlockChain() {
		lightBlockID = cpt;
		cpt++;
	}
	
	public void addLightHeader(Header h) {
		String headerHash = h.getHeaderHash();
		String previousHeaderHash = h.getPrevHash();
		LightBlock ln = new LightBlock(previousHeaderHash, headerHash);
		LightBlkchain.add(ln);
		if (LightBlkchain.size() == 11) {
			LightBlkchain.remove(0);
		}
	}
}