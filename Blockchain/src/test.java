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
        Node n1 = new Node("Charles", blk);
        blk.addNoeud(n1);
        Node n2 = new Node("Corentin", blk);
        blk.addNoeud(n2);
        Node n3 = new Node("Laura", blk);
        blk.addNoeud(n3);
        Node n4 = new Node("James", blk);
        blk.addNoeud(n4);
        Node n5 = new Node("Manu", blk);
        blk.addNoeud(n5);
        Node n6 = new Node("Jacques", blk);
        blk.addNoeud(n6);
        Node n7 = new Node("Elsa Lesfleur", blk);
        blk.addNoeud(n7);
        Node n8 = new Node("Rose Lesfleur", blk);
        blk.addNoeud(n8);
        Node n9 = new Node("Tobi", blk);
        blk.addNoeud(n9);
        Node n10 = new Node("Wilson", blk);
        blk.addNoeud(n10);
        Node n11 = new Node("Taki", blk);
        blk.addNoeud(n11);
        Node n12 = new Node("Werner", blk);
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
