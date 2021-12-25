package project;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Blockchain {

    private List<Block> blockchain;

    public Blockchain(){
        blockchain = new CopyOnWriteArrayList<Block>();
    }

    public boolean add(Block block){
        return blockchain.add(block);
    }

    public List<Block> getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(List<Block> blockchain) {
        this.blockchain = blockchain;
    }
}
