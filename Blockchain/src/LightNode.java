import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class LightNode extends Node{
	private double wallet;
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private KeyPair keys;
	private Network network;

	public LightNode(String nom, Network network) {
		super(nom, network);
		this.wallet = 0;
		try {
			keys = RsaUtil.generateKeyPair();
			publicKey = keys.getPublic();
			privateKey = keys.getPrivate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void sendMoneyTo(double amount, int nodeId) {
		if (wallet < amount) {
			System.out.println(nom + " n'a pas assez de monnaie pour vendre");
			System.out.println("Rejected transaction");
		} else {
			network.broadcastTransaction(new Transaction("", this.nodeId, nodeId, amount, System.currentTimeMillis(), privateKey));
		}
	}

	public double getWallet() {
		return wallet;
	}

	public PublicKey getPublicKey(){ return keys.getPublic();}
}
