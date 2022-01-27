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

        FullNode s1 = new FullNode("Spain Server", bitcoin);
        FullNode s2 = new FullNode("Alex Server", bitcoin);
        FullNode s3 = new FullNode("Paris Server", bitcoin);
        FullNode s4 = new FullNode("England Server", bitcoin);

        Miner m1 = new Miner("A", bitcoin);
        Miner m2 = new Miner("B", bitcoin);
        Miner m3 = new Miner("C", bitcoin);

        ExecutorService executor = Executors.newFixedThreadPool(4);
        executor.execute(m1);
        executor.execute(m2);
        executor.execute(m3);

        for (int i = 1; i < 100; i++) {
            ln1.sendMoneyTo(i * 0.1, ln2.getNodeId());
        }
		/*
		ln1.sendMoneyTo(1, ln2.getNodeId());
		ln1.sendMoneyTo(2, ln2.getNodeId());
		ln2.sendMoneyTo(3, ln1.getNodeId());
		ln2.sendMoneyTo(4, ln2.getNodeId());
		ln2.sendMoneyTo(5, ln3.getNodeId());

		ln2.sendMoneyTo(6, ln4.getNodeId());
		ln2.sendMoneyTo(7, ln5.getNodeId());
		ln2.sendMoneyTo(8, ln6.getNodeId());
		ln2.sendMoneyTo(9, ln1.getNodeId());
		ln6.sendMoneyTo(10, ln2.getNodeId());

		ln6.sendMoneyTo(11, ln2.getNodeId());
		ln6.sendMoneyTo(12, ln2.getNodeId());
		ln6.sendMoneyTo(13, ln2.getNodeId());
		ln2.sendMoneyTo(14, ln2.getNodeId());
		ln5.sendMoneyTo(15, ln2.getNodeId());
		ln4.sendMoneyTo(16, ln2.getNodeId());
		ln3.sendMoneyTo(17, ln2.getNodeId());

		 */
    }
}
