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
        Noeud n1 = new Noeud("Charles", blk);
        blk.addNoeud(n1);
        Noeud n2 = new Noeud("Corentin", blk);
        blk.addNoeud(n2);
        Noeud n3 = new Noeud("Laura", blk);
        blk.addNoeud(n3);
        Noeud n4 = new Noeud("James", blk);
        blk.addNoeud(n4);
        Noeud n5 = new Noeud("Manu", blk);
        blk.addNoeud(n5);
        Noeud n6 = new Noeud("Jacques", blk);
        blk.addNoeud(n6);
        Noeud n7 = new Noeud("Elsa Lesfleur", blk);
        blk.addNoeud(n7);
        Noeud n8 = new Noeud("Rose Lesfleur", blk);
        blk.addNoeud(n8);
        Noeud n9 = new Noeud("Tobi", blk);
        blk.addNoeud(n9);
        Noeud n10 = new Noeud("Wilson", blk);
        blk.addNoeud(n10);
        Noeud n11 = new Noeud("Taki", blk);
        blk.addNoeud(n11);
        Noeud n12 = new Noeud("Werner", blk);
        blk.addNoeud(n12);

        n3.acheter(100);
        n5.vendre(50);
        n8.acheter(500);
        n7.vendre(100);
        n1.vendre(1000);
        n1.vendre(50);
        n9.acheter(1000);
        n10.acheter(250);
        n9.vendre(1100);
        n1.acheter(10000);

        blk.printBlk();
        blk.printNodes();
    }


}
