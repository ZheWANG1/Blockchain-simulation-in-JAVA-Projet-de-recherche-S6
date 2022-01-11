public class test {

    public static void main(String[] args) {
        //simulationPow();
        simulationPoS();
    }

    private static void simulationPow() {
        int numberOfTransaction = 10;
        Blockchain blk = new Blockchain();
        for (int i = 0; i < 50; i++) {
            blk.newTransactionPoW("TestTransaction");
        }
        blk.printBlk();
    }

    private static void simulationPoS() {
        Blockchain blk = new Blockchain();
        Miner n1 = new Miner("Charles", blk);
        blk.addNoeud(n1);
        Miner n2 = new Miner("Corentin", blk);
        blk.addNoeud(n2);
        Miner n3 = new Miner("Laura", blk);
        blk.addNoeud(n3);
        Miner n4 = new Miner("James", blk);
        blk.addNoeud(n4);
        Miner n5 = new Miner("Manu", blk);
        blk.addNoeud(n5);
        Miner n6 = new Miner("Jacques", blk);
        blk.addNoeud(n6);
        Miner n7 = new Miner("Elsa Lesfleur", blk);
        blk.addNoeud(n7);
        Miner n8 = new Miner("Rose Lesfleur", blk);
        blk.addNoeud(n8);
        Miner n9 = new Miner("Tobi", blk);
        blk.addNoeud(n9);
        Miner n10 = new Miner("Wilson", blk);
        blk.addNoeud(n10);
        Miner n11 = new Miner("Taki", blk);
        blk.addNoeud(n11);
        Miner n12 = new Miner("Werner", blk);
        blk.addNoeud(n12);

        n3.buy(100);
        n5.sell(50);
        n8.buy(500);
        n7.sell(100);
        n1.sell(1000);
        n1.sell(50);
        n9.buy(1000);
        n10.buy(250);
        n9.sell(1100);
        n1.buy(10000);

        blk.printBlk();
        blk.printNodes();
    }


}
