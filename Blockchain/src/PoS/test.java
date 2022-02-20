package PoS;

import Network.Network;
import Network.Validator;
import Network.ValidatorNode;


public class test {

    public static void main(String[] args) {
        simulationPoS();
    }

    public static void simulationPoS() {
        Network bitcoin = new Network();

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

        ln1.stake(20);
        ln2.stake(25);
        ln3.stake(20);
        ln4.stake(50);
        ln5.stake(70);
        ln6.stake(30);

        vn1.addInvestor(ln1.getNodeAddress(), ln1.getStakeAmount());


        Validator validatorExec = new Validator(bitcoin);
        new Thread(validatorExec).start();



        for (int i = 0; i < 1; i++) {
            ln1.sendMoneyTo(1,ln3.getNodeAddress());
        }
    }
}
