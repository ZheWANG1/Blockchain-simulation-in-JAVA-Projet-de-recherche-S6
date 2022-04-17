package PoS;

import Network.Network;
import Network.Validator;
import Network.ValidatorNode;
import Network.ValidatorParaL;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;


public class test {

    public static void main(String[] args) {
        try {
            simulationPoS();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void simulationPoS() throws FileNotFoundException {
        PrintStream FileResult = new PrintStream("ResultParaLBlockchain");
        System.setOut(FileResult);
        String TYPE1 = "Bit1";
        String TYPE2 = "Bit2";

        Network bitcoin = new Network(TYPE1, TYPE2);
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
        ValidatorNode vn1 = new ValidatorNode("Validator 1", bitcoin, wvn1);
        ValidatorNode vn2 = new ValidatorNode("Validator 2", bitcoin, wvn2);
        ValidatorNode vn3 = new ValidatorNode("Validator 3", bitcoin, wvn3);
        ValidatorNode vn4 = new ValidatorNode("Validator 4", bitcoin, wvn4);
        ValidatorNode vn5 = new ValidatorNode("Validator 5", bitcoin, wvn5);
        ValidatorNode vn6 = new ValidatorNode("Validator 6", bitcoin, wvn6);

        // Normal lightNode
        LightNode ln1 = new LightNode("Jack", bitcoin);
        LightNode ln2 = new LightNode("Alex", bitcoin);
        LightNode ln3 = new LightNode("Chen", bitcoin);
        LightNode ln4 = new LightNode("Kyle", bitcoin);
        LightNode ln5 = new LightNode("Jennifer", bitcoin);
        LightNode ln6 = new LightNode("Lise", bitcoin);

        ln1.stake(20, TYPE1);
        ln2.stake(25, TYPE1);
        ln3.stake(20, TYPE1);
        ln4.stake(50, TYPE1);
        ln5.stake(70, TYPE1);
        ln6.stake(30, TYPE1);

        ln1.stake(10, TYPE2);
        ln2.stake(25, TYPE2);
        ln3.stake(40, TYPE2);
        ln4.stake(5, TYPE2);
        ln5.stake(30, TYPE2);
        ln6.stake(60, TYPE2);

        vn1.addInvestorType(ln1.getNodeAddress(), ln1.getStakeAmount1(), TYPE1);
        vn2.addInvestorType(ln2.getNodeAddress(), ln2.getStakeAmount1(), TYPE1);
        //vn3.addInvestorType(ln3.getNodeAddress(), ln3.getStakeAmount1(), TYPE1);
        vn4.addInvestorType(ln4.getNodeAddress(), ln4.getStakeAmount1(), TYPE1);
        vn5.addInvestorType(ln5.getNodeAddress(), ln5.getStakeAmount1(), TYPE1);
        vn6.addInvestorType(ln6.getNodeAddress(), ln6.getStakeAmount1(), TYPE1);

        vn1.addInvestorType(ln1.getNodeAddress(), ln1.getStakeAmount1(), TYPE2);
        //vn2.addInvestorType(ln2.getNodeAddress(), ln2.getStakeAmount1(), TYPE2);
        vn3.addInvestorType(ln3.getNodeAddress(), ln3.getStakeAmount1(), TYPE2);
        //vn4.addInvestorType(ln4.getNodeAddress(), ln4.getStakeAmount1(), TYPE2);
        vn5.addInvestorType(ln5.getNodeAddress(), ln5.getStakeAmount1(), TYPE2);
        vn6.addInvestorType(ln6.getNodeAddress(), ln6.getStakeAmount1(), TYPE2);

        //Validator validatorExec = new Validator(bitcoin);
        //new Thread(validatorExec).start();

        ValidatorParaL validatorExec = new ValidatorParaL(bitcoin);
        new Thread(validatorExec).start();


        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 100; j++) {
                ln1.sendMoneyTo(100, ln3.getNodeAddress(), TYPE1);
                if(j < 60)
                    ln3.sendMoneyTo(200, ln3.getNodeAddress(), TYPE2);
                try {
                    Thread.sleep(100);
                }catch(InterruptedException e){
                    System.out.println(e);
                }
            }
            System.out.println("------------Nb of block 1 : " + Network.NB_OF_BLOCK_OF_TYPE1_CREATED+"\n"+"--------------Nb of block 2 : "+ Network.NB_OF_BLOCK_OF_TYPE2_CREATED);
            if (Network.NB_OF_BLOCK_OF_TYPE1_CREATED + Network.NB_OF_BLOCK_OF_TYPE2_CREATED >= 5){
                System.out.println("Finishing tests");
                System.exit(1);
            }
        }
    }
}
