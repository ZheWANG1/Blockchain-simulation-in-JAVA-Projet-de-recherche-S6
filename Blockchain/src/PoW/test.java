package PoW;

import Network.Network;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class test {

    public static void main(String[] args) {
        simulationPoW();
    }

    public static void simulationPoW() {
        Network bitcoin = new Network();

        PoW.LightNode ln1 = new LightNode("Jack", bitcoin);
        PoW.LightNode ln2 = new LightNode("Alex", bitcoin);
        PoW.LightNode ln3 = new LightNode("Chen", bitcoin);
        PoW.LightNode ln4 = new LightNode("Kyle", bitcoin);

        PoW.FullNode s1 = new FullNode("Spain Server", bitcoin, false);
        PoW.FullNode s2 = new FullNode("Alex Server", bitcoin, false);
        PoW.FullNode s3 = new FullNode("Paris Server", bitcoin, false);
        PoW.FullNode s4 = new FullNode("England Server1", bitcoin, false);
        PoW.FullNode s5 = new FullNode("England Server2", bitcoin, false);
        PoW.FullNode s6 = new FullNode("England Server3", bitcoin, false);
        PoW.FullNode s7 = new FullNode("England Server4", bitcoin, false);
        PoW.FullNode s8 = new FullNode("England Server5", bitcoin, false);

        PoW.FullNode m1 = new FullNode("Miner from England", bitcoin, true);
        PoW.FullNode m2 = new FullNode("Miner from Mexico", bitcoin, true);
        PoW.FullNode m3 = new FullNode("Miner from Paris", bitcoin, true);
        PoW.FullNode m4 = new FullNode("Miner from Spain1", bitcoin, true);
        PoW.FullNode m5 = new FullNode("Miner from Spain2", bitcoin, true);
        PoW.FullNode m6 = new FullNode("Miner from Spain3", bitcoin, true);
        PoW.FullNode m7 = new FullNode("Miner from Spain4", bitcoin, true);
        PoW.FullNode m8 = new FullNode("Miner from Spain5", bitcoin, true);




        ExecutorService executor = Executors.newFixedThreadPool(8);
        executor.execute(m1);
        executor.execute(m2);
        executor.execute(m3);
        executor.execute(m4);
        executor.execute(m5);
        executor.execute(m6);
        executor.execute(m7);
        executor.execute(m8);

        for (int i = 0; i < 20; i++) {
            ln1.sendMoneyTo(0.1 * i, ln2.getNodeAddress());
        }
    }
}
