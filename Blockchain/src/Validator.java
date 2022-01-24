import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Validator extends Node{
    private final List<Transaction> transactionBuffer = new CopyOnWriteArrayList<>();
    private final int clientID = 0;
    private LightNode validator = null;
    private final int nbMax  = 10;


    public Validator(String name, Network network) {
        super(name, network, new Blockchain());
        chooseValidator();
    }

    public void chooseValidator(){
        List<Node> listNode = network.getNetwork();
        double possibility = 0;
        LightNode candidate = null;
        for (Node node : listNode){
            if(node instanceof LightNode) {
                double stakeAmount = ((LightNode)node).getStakeAmount();
                double stakeTime = ((LightNode)node).getStakeTime();
                if (stakeAmount + stakeTime >= possibility){ // il faut modifier
                    candidate = ((LightNode) node);
                    possibility = stakeAmount + stakeTime;
                }
            }
        }
        validator = candidate;
    }

    public boolean verifySignature(Transaction transaction) throws Exception {
        return RsaUtil.verify("", transaction.getSignature(), network.getPkWithID(transaction.getFromID()));
    }

    public void validate() {
        new Thread(()->{
        while(true) {
            if (transactionBuffer.size() >= nbMax) {
                int i = 0;
                while (i != nbMax)
                    try {
                        if (verifySignature(transactionBuffer.get(i))) {
                            i++;
                        } else {
                            System.out.println(transactionBuffer.get(i) + "is false");
                            transactionBuffer.remove(i);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
            List<Transaction> transactionsInBlock = transactionBuffer.subList(0, nbMax - 1);
            Block block = new Block(blockchain.getLatestBlock(), transactionsInBlock);
            String hash = block.getHeader().calcHeaderHash(0);
            block.getHeader().setHeaderHash(hash);
            //System.out.println(name + " " + hash);
            try {
                block.setNodeID(nodeId);
                network.broadcastBlock(block, RsaUtil.sign("", this.privateKey), nodeId, blockchain);
            } catch (Exception e) {
                e.printStackTrace();
            }
            transactionBuffer.removeAll(transactionsInBlock);
            chooseValidator();
        }}).start();
    }


    public void receiptTransaction(Transaction transaction) {
        transactionBuffer.add(transaction);
    }

    public void receiptBlock(Block b){}
}

