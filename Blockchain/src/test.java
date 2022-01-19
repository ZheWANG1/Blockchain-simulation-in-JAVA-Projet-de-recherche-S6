import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class test {

    public static void main(String[] args) {
    	simulation();
    }

    public static void simulation() {
    	Network bitcoin = new Network();
    	
    	LightNode ln1 = new LightNode("Jack",bitcoin);
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

		ExecutorService executor = Executors.newFixedThreadPool(3);
		executor.execute(m1);
		executor.execute(m2);
		executor.execute(m3);

		ln1.sendMoneyTo(10, 2);
    	
    	
    	
    }
}
