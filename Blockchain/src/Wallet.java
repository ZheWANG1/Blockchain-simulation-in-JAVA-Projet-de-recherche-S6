public class Wallet {
    private final Node user;
    private double account;

    public Wallet(Node user) {
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
