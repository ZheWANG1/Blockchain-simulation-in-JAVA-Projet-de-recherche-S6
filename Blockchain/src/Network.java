import javax.swing.text.html.HTMLDocument;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Network {
	private final List<Node> network = new ArrayList<>();
	private final Map<Integer, PublicKey> keyTable = new HashMap<>();

	public Network() {
	}

	public boolean addNode(Node node){
		try {
			network.add(node);
			keyTable.put(node.getNodeId(), node.getPublicKey());
			return true;
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

	public void broadcastTransaction(Transaction transaction){
		for(int i = 0; i < network.size() ; i++){
			network.get(i).addTransaction(transaction);
		}
	}

	public void broadcastBlock(Block b){
		for(int i = 0; i < network.size() ; i++){
			network.get(i).addBlock(b);
		}
	}

	public PublicKey getPkWithID(int id){
		return keyTable.get(id);
	}
}
