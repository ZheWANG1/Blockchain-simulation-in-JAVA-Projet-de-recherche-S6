import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LightNode extends Node {
    private double wallet;
    private List<Transaction> transactionBuffer = new CopyOnWriteArrayList<>();

    public LightNode(String name, Network network) {
        super(name, network,new LightBlockChain());
        this.wallet = 80;
        try {
            keys = RsaUtil.generateKeyPair();
            publicKey = keys.getPublic();
            privateKey = keys.getPrivate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        network.addNode(this);
    }

    public void sendMoneyTo(double amount, int nodeId) {
        if (wallet < amount) {
            System.out.println(name + " Not enough bitcoin to send"); // Whatever the currency
            System.out.println("Rejected transaction");
        } else {
            Transaction toSend = new Transaction("", this.nodeId, nodeId, amount, System.currentTimeMillis(), 0.1, privateKey);
            network.broadcastTransaction(toSend);
            transactionBuffer.add(toSend);
        }
    }

    public void checkIfAllTransSent(Block b){
        List<Transaction> transSent = new ArrayList<>();
        for(Transaction t: transactionBuffer){
            List<Transaction> trans = b.getTransaction();
            if(!trans.contains(t)){
                network.broadcastTransaction(t);
            }else{
                transSent.add(t);
            }
        }
        transactionBuffer.removeAll(transSent);
    }

    public double getWallet() {
        return wallet;
    }

    public void receiptCoin(double amount) {
        String order = amount < 0 ? " Lost " : " received ";

        wallet += amount;
        System.out.println(this.name + order + amount + " bitcoins");
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void receiptBlock(Block b) {
        ((LightBlockChain)blockchain).addLightHeader(b.getHeader());
        //checkIfAllTransSent(b);
    }
}
