package PoS;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        LightNode ln1 = new LightNode("Jack", bitcoin);
        LightNode ln2 = new LightNode("Alex", bitcoin);
        LightNode ln3 = new LightNode("Chen", bitcoin);
        LightNode ln4 = new LightNode("Kyle", bitcoin);
        LightNode ln5 = new LightNode("Jennifer", bitcoin);
        LightNode ln6 = new LightNode("Lise", bitcoin);

        ExecutorService executor = Executors.newFixedThreadPool(1);

        Validator validator = new Validator(bitcoin);
        executor.execute(validator);
        validator.run();

        for (int i = 1; i < 100; i++) {
            ln1.sendMoneyTo(i * 0.1, ln2.getNodeId());
        }
    }
}