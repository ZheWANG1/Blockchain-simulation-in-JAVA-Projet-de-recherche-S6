
import HashUtil
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
		Body b = blockPrev.getBody()
		String trans = b.getTransaction()
		blockTransHash = HashUtil.SHA256(trans)
		Header h = blockPrev.getHeader()
		headerHashPrev = h.getHeaderHash()
	};
	
	public Header() {
		
		
	}
	
	public int getBlockId() {return blockId;}
	
	public int getNonce() {return nonce;}
	
	public void calcHeaderHash(Block blockPrev) {
		String concat = headerHashPrev + blockTransHash
		String minedBlock = mineBlock(concat)
	}
	
	public int getPrevHash() {return headerHashPrev;}
	
	public int getBlockTransHash() {return blockTransHash;}
	
	public int getBodyId( ) {return bodyId;}
	
	public String mineBlock() {
		
		
	}
	
	
	
	
}
