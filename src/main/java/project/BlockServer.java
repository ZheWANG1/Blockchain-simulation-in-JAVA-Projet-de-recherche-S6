package project;

public class BlockServer {

    private Blockchain blockchain;

    public void createGenesisBlock(){
        Block genesisBlock = new Block();
        genesisBlock.setIndex(1);
        genesisBlock.setTime(System.currentTimeMillis());
        //
        genesisBlock.setHash(genesisBlock.calculateHash("", genesisBlock.getLedger()));


        blockchain.add(genesisBlock);
    }

}
