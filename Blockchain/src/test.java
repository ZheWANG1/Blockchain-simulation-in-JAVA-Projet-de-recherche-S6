public class test {
	public static void main(String[] args) {
		Blockchain bitcoin = new Blockchain();
		bitcoin.newTransaction("Achat de 600 bitcoins");
		bitcoin.newTransaction("Transfert de 250 bitcoins");
		bitcoin.newTransaction("Vente de 120 bitcoins");
		bitcoin.printBlk();
		
	}
}
