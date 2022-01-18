import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

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
	
	public boolean chainValidation() {
        Iterator<LightBlock> it = LightBlkchain.iterator();
        LightBlock blockprev = it.next();
        while (it.hasNext()) {
            LightBlock block = it.next();
            if (!Objects.equals(block.getHeader().getPrevHash(), blockprev.getHeader().getHeaderHash()))
                return false;
            blockprev = block;
        }
        return true;
    }
	
	
	
}
