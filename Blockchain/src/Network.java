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

	public boolean addNode(Node node) {
		network.add(node);
		try {
			if (node instanceof LightNode)
				keyTable.put(node.getNodeId(), ((LightNode) node).getPublicKey());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void broadcastTransaction(Transaction transaction) {
		for (int i = 0; i < network.size(); i++) {
			network.get(i).addTransaction(transaction);
		}
	}

	public void broadcastBlock(Block b) {
		for (int i = 0; i < network.size(); i++) {
			if (network.get(i) instanceof FullNode)
				((FullNode) network.get(i)).addBlock(b);
		}
	}

	public PublicKey getPkWithID(int id) {
		return keyTable.get(id);
	}

	public void broadcastReward(int clientId) {
		int i = 0;
		Node associatedLightNode = network.get(i);
		while (associatedLightNode.getNodeId() != clientId) {
			i++;
			associatedLightNode = network.get(i);
		}
		// broadcast

	}

	public Blockchain copyBlockchainFromFN() {
		int i = 0;
		Node node = network.get(i);
		while (!(node instanceof FullNode)) {
			i++;
			node = network.get(i);
		}
		return ((FullNode) node).getBlockchain();
	}
}
