

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.math.*;
public class Header {
	private int blockId; // Identifiant du blockId
	
	private int headerId; // Identifiant du Header 	
	
	private int bodyId; // Identifiant du corps
	
	private int nonce; // Random number
	
	private Date timeStamp; // Date de création du block
	
	private int blockTransHash;
	
	private int headerHash;
	
	private int headerHashPrev;
	
	public Header(int headerHashPrev) {
		nonce = ThreadLocalRandom.current().nextInt(0, 10000);
		timeStamp = new Date(System.currentTimeMillis());
		
	};
	
	public int getBlockId() {return blockId;}
	
	public int getNonce() {return nonce;}
	
	public void calcHeaderHash() {
		
	}
	
	public int getPrevHash() {return headerHashPrev;}
	
	public int getBlockTransHash() {return blockTransHash;}
	
	public int getBodyId( ) {return bodyId;}
	
	public void mineBlock() {
		
		
	}
	
	
	
	
}
