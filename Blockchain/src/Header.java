

import java.util.concurrent.ThreadLocalRandom;

public class Header {
	
	private final int nbOfZeros = 5; 
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
		
		calcHeaderHash();
	};
	
	public Header() {
		timeStamp = System.currentTimeMillis();
		blockTransHash = "";
		headerHashPrev = "";
		
		calcHeaderHash();
	}
	
	public int getNonce() {return nonce;}
	
	public void calcHeaderHash() {
		String concat = headerHashPrev + blockTransHash;
		String minedBlock = mineBlock(concat);
		headerHash = minedBlock;
	}
	
	
	
	public String mineBlock(String s) {
		while (true){
			nonce = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
			String test_hash = s + Integer.toString(nonce);
			test_hash = HashUtil.SHA256(test_hash);
			String toBeCheckedSubList = test_hash.substring(0,nbOfZeros);
			if( toBeCheckedSubList.equals("0".repeat(nbOfZeros))) {
				return test_hash;
			}
		}

		
	}
	public String getPrevHash() {return headerHashPrev;}
	
	public String getBlockTransHash() {return blockTransHash;}
	
	public String getHeaderHash( ) {return headerHash;}
	
	public String toString() {
		return "\nTS : "+timeStamp + "\nhash : "+headerHash + "\nNonce : "+ nonce;
	}
	
	
}
