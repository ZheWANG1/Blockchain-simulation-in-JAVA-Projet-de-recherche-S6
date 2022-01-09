
import HashUtil
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.math.*;
public class Header {
	
	private final int nbOfZeros = 5  
	private int nonce; // Nombre aléatoire permmetant de generé un strign aleatoire
	
	private long timeStamp; // Date de création du block
	
	private String blockTransHash;
	
	private String headerHash;
	
	private String headerHashPrev;
	
	public Header(Block blockPrev) {
		timeStamp = System.currentTimeMillis();
		
		Body b = blockPrev.getBody();
		String trans = b.getTransaction();
		blockTransHash = HashUtil.SHA256(trans);
		
		Header h = blockPrev.getHeader();
		headerHashPrev = h.getHeaderHash();
	};
	
	public Header() {
		
		
	}
	
	public int getBlockId() {return blockId;}
	
	public int getNonce() {return nonce;}
	
	public void calcHeaderHash(Block blockPrev) {
		String concat = headerHashPrev + blockTransHash;
		String minedBlock = mineBlock(concat);
		headerHash = minedBlock;
	}
	
	public int getPrevHash() {return headerHashPrev;}
	
	public int getBlockTransHash() {return blockTransHash;}
	
	public int getBodyId( ) {return bodyId;}
	
	public String mineBlock(String s) {
		while true{
			nonce = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
			test_hash = s + nonce.toString();
			test_hash = HashUtil.SHA256(test_hash);
			toBeCheckedSubList = test_hash.sublist(0,nbOfZeros);
			if( toBeCheckedSubList.equals("0".repeat(nbOfZeros))) {
				return test_hash;
			}
		}

		
	}
	
	
	
	
}
