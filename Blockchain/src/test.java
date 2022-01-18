public class test {

    public static void main(String[] args) {
    	
    }

    public void Simulation() {
    	Network bitcoin = new Network();
    	
    	LightNode ln1 = new LightNode("Jack",bitcoin);
    	LightNode ln2 = new LightNode("Alex", bitcoin);
    	LightNode ln3 = new LightNode("Chen", bitcoin);
    	LightNode ln4 = new LightNode("Kyle", bitcoin);
    	LightNode ln5 = new LightNode("Jennifer", bitcoin);
    	LightNode ln6 = new LightNode("Lise", bitcoin);
    	
    	FullNode s1 = new FullNode("Spain Server");
    	FullNode s2 = new FullNode("Alex Server");
    	FullNode s3 = new FullNode("Paris Server");
    	FullNode s4 = new FullNode("England Server");
    	
    	
    	
    	
    }
}
