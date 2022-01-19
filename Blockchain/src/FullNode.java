
public class FullNode extends Node {

    private Blockchain blockchain;

    public FullNode(String nom, Network network) {
        super(nom,network);
        blockchain = new Blockchain();
    }

    public Blockchain getBlockchain(){
        return blockchain;
    }

    public void receiptBlock(Block block){
        blockchain.addBlock(block);
    }
    
}
