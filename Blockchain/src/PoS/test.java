package PoS;

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

        Validator validator = new Validator(bitcoin);
        new Thread(validator).start();

        ln1.stake(20);
        ln2.stake(5);
        ln3.stake(20);
        ln4.stake(50);
        ln5.stake(80);
        ln6.stake(1);

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 10; j++)
                ln1.sendMoneyTo((i + 1) * 0.1, ln2.getNodeAddress());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
