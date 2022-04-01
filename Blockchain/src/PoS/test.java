package PoS;

import Network.Network;
import Network.Validator;
import Network.ValidatorNode;


public class test {

    public static void main(String[] args) {
        simulationPoS();
    }

    public static void simulationPoS() {
        Network bitcoin = new Network("1", "2");
        bitcoin.mode = "POS";

        FullNode s1 = new FullNode("Spain Server", bitcoin);
        FullNode s2 = new FullNode("Alex Server", bitcoin);
        FullNode s3 = new FullNode("Paris Server", bitcoin);
        FullNode s4 = new FullNode("England Server", bitcoin);

        // Validator Wallet
        LightNode wvn1 = new LightNode("Validator wallet1", bitcoin);
        LightNode wvn2 = new LightNode("Validator wallet2", bitcoin);
        LightNode wvn3 = new LightNode("Validator wallet3", bitcoin);
        LightNode wvn4 = new LightNode("Validator wallet4", bitcoin);
        LightNode wvn5 = new LightNode("Validator wallet5", bitcoin);
        LightNode wvn6 = new LightNode("Validator wallet6", bitcoin);
        // Validator
        ValidatorNode vn1 = new ValidatorNode("Validator 1",bitcoin, wvn1);
        ValidatorNode vn2 = new ValidatorNode("Validator 2",bitcoin, wvn2);
        ValidatorNode vn3 = new ValidatorNode("Validator 3",bitcoin, wvn3);
        ValidatorNode vn4 = new ValidatorNode("Validator 4",bitcoin, wvn4);
        ValidatorNode vn5 = new ValidatorNode("Validator 5",bitcoin, wvn5);
        ValidatorNode vn6 = new ValidatorNode("Validator 6",bitcoin, wvn6);

        // Normal lightNode
        LightNode ln1 = new LightNode("Jack", bitcoin);
        LightNode ln2 = new LightNode("Alex", bitcoin);
        LightNode ln3 = new LightNode("Chen", bitcoin);
        LightNode ln4 = new LightNode("Kyle", bitcoin);
        LightNode ln5 = new LightNode("Jennifer", bitcoin);
        LightNode ln6 = new LightNode("Lise", bitcoin);

        ln1.stake(20,"1");
        ln2.stake(25,"1");
        ln3.stake(20,"1");
        ln4.stake(50,"1");
        ln5.stake(70,"1");
        ln6.stake(30,"1");

        ln1.stake(10,"2");
        ln2.stake(25,"2");
        ln3.stake(40,"2");
        ln4.stake(5,"2");
        ln5.stake(30,"2");
        ln6.stake(60,"2");

        vn1.addInvestorType(ln1.getNodeAddress(), ln1.getStakeAmount1(), "1");
        vn2.addInvestorType(ln2.getNodeAddress(), ln2.getStakeAmount1(), "1");
        vn3.addInvestorType(ln3.getNodeAddress(), ln3.getStakeAmount1(), "1");
        vn4.addInvestorType(ln4.getNodeAddress(), ln4.getStakeAmount1(), "1");
        vn5.addInvestorType(ln5.getNodeAddress(), ln5.getStakeAmount1(), "1");
        vn6.addInvestorType(ln6.getNodeAddress(), ln6.getStakeAmount1(), "1");

        vn1.addInvestorType(ln1.getNodeAddress(), ln1.getStakeAmount1(), "2");
        vn2.addInvestorType(ln2.getNodeAddress(), ln2.getStakeAmount1(), "2");
        vn3.addInvestorType(ln3.getNodeAddress(), ln3.getStakeAmount1(), "2");
        vn4.addInvestorType(ln4.getNodeAddress(), ln4.getStakeAmount1(), "2");
        //vn5.addInvestorType(ln5.getNodeAddress(), ln5.getStakeAmount1(), "2");
        vn6.addInvestorType(ln6.getNodeAddress(), ln6.getStakeAmount1(), "2");


        Validator validatorExec = new Validator(bitcoin);
        new Thread(validatorExec).start();

        for (int i = 0; i < 20; i++) {
            for(int j=0; j < 10; j++)
                ln1.sendMoneyTo(2,ln3.getNodeAddress(), "1");
            ln1.sendMoneyTo(2,ln3.getNodeAddress(), "2");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
