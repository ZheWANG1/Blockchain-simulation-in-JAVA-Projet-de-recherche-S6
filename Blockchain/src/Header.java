

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class Header {
	private int blockId;
	private int headerId;	
	private int bodyId;
	private int nonce;
	private Date timeStamp = new Date();
	private int blockTransHash;
	private int headerHash;
	private int headerHashPrev;
	public Header() {
		
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
