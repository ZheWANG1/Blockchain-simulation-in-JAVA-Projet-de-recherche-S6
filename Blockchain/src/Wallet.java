public class Wallet {
    private final Miner user;
    private double account;

    public Wallet(Miner user) {
        this.user = user;
        this.account = 100;
    }

    public void add(double nbMoney) {
        this.account += nbMoney;
    }

    public void reduce(double nbMoney) {
        this.account -= nbMoney;
    }

    public double getAccount() {
        return account;
    }

}
