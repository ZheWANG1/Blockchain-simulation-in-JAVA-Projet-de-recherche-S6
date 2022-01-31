package PoW;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class test {

    public static void main(String[] args) {
        simulationPoW();
    }

    public static void simulationPoW() {
        Network bitcoin = new Network();

        LightNode ln1 = new LightNode("Jack", bitcoin);
        LightNode ln2 = new LightNode("Alex", bitcoin);
        LightNode ln3 = new LightNode("Chen", bitcoin);
        LightNode ln4 = new LightNode("Kyle", bitcoin);
        LightNode ln5 = new LightNode("Jennifer", bitcoin);
        LightNode ln6 = new LightNode("Lise", bitcoin);

        FullNode s1 = new FullNode("Spain Server", bitcoin, false);
        FullNode s2 = new FullNode("Alex Server", bitcoin, false);
        FullNode s3 = new FullNode("Paris Server", bitcoin, false);
        FullNode s4 = new FullNode("England Server", bitcoin, false);
        FullNode m1 = new FullNode("Miner from England", bitcoin, true);
        FullNode m2 =  new FullNode("Miner from Mexico", bitcoin, true);
        FullNode m3 = new FullNode("Miner from Paris", bitcoin, true);


        ExecutorService executor = Executors.newFixedThreadPool(10);
        executor.execute(m1);
        executor.execute(m2);
        executor.execute(m3);


		for (int i = 0; i < 1000; i++) {
            ln1.sendMoneyTo( 0.1, ln2.getNodeId());
        }
        System.out.println("All transaction are sent");


    }
}
