
public class FullNode extends Node {

    public FullNode(String nom, Network network) {
        super(nom,network, new Blockchain());
        network.addNode(this);
    }

    public void receiptBlock(Block block, String signature, int nodeID, Blockchain blk){
        this.blockchain.addBlock(block);
    }

}
