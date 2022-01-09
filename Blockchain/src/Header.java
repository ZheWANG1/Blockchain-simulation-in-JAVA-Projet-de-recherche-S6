

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.math.*;
public class Header {
	
	private int nonce; // Random number
	
	private Date timeStamp; // Date de création du block
	
	private String blockTransHash;
	
	private String headerHash;
	
	private String headerHashPrev;
	
	public Header(Block blockPrev) {
		nonce = ThreadLocalRandom.current().nextInt(0, 10000);
		timeStamp = new Date(System.currentTimeMillis());
		headerHash = calcHeaderHash(blockPrev)
		
	};
	
	public Header() {
		
		
	}
	
	public int getBlockId() {return blockId;}
	
	public int getNonce() {return nonce;}
	
	public void calcHeaderHash(Block blockPrev) {
		String body = blockPrev.
	}
	
	public int getPrevHash() {return headerHashPrev;}
	
	public int getBlockTransHash() {return blockTransHash;}
	
	public int getBodyId( ) {return bodyId;}
	
	public void mineBlock() {
		
		
	}
	
	
	
	
}
