package PoS;

import java.util.ArrayList;

/*
   Une node validator est comme une fullNode
   Sont objectif est de créer les blocks et de verifier les transactions
   Ainsi on verififira chaques blocs ainsi que chaque transaction
   Lorsque que ce validator sera choisi il forgera un block et le broadcastera celui-ci alors passera
   par le consensus et sera validée
   Des LightNode peuvent decider de miser sur la current fullNode
   Plus le stake misé est grand plus la validator node aura de probabilité d'être choisie
   la FullNode sera alors recompensée et les LightNode receveront une part du gain
 */
public class ValidatorNode extends FullNode{
    private ArrayList<Transaction> pendingTransaction = new ArrayList<>();
    private LightNode fullNodeAccount;
    private String[] investorList;
    /**
     * Constructor FullNode
     *
     * @param nom     Name of the FullNode
     * @param network Network of the FullNode, one node is connected to the whole network (P2P)
     */
    public ValidatorNode(String nom, Network network, LightNode fullNodeAccount) {
        super(nom, network);
        this.fullNodeAccount = fullNodeAccount;
    }

    public void receiptTransaction(Transaction T){}
    public void verifyTransaction(Transaction t){}
    public void forgeBlock(){}
    public void getAndBroadcastReward(double amount){}
    public void addInvestor(String investorAdress){}
    public void delInvestor(String investorAdress){}
}
