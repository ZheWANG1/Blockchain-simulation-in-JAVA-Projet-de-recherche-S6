import java.nio.file.Watchable;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class LightNode extends Node{
	private double wallet;
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private KeyPair keys;
	private Network network;

	public LightNode(String name, Network network) {
		super(name, network);
		this.wallet = 80;
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
			System.out.println(name + " Not enough bitcoin to send"); // Whatever the currency
			System.out.println("Rejected transaction");
		} else {
			network.broadcastTransaction(new Transaction("", this.nodeId, nodeId, amount, System.currentTimeMillis(),0.1, privateKey));
		}
	}

	public double getWallet() {
		return wallet;
	}

	public void receiveReward(int amount){
		wallet += amount;
		System.out.println(this.name + " received "+amount+" bitcoins as reward");
	}

	public PublicKey getPublicKey(){ return keys.getPublic();}
}
