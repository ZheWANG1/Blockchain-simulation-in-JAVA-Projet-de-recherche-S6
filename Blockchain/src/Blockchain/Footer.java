package Blockchain;

public class Footer {

    private String prevHash;

    public Footer(String prevHash) {
        this.prevHash = prevHash;
    }

    public Footer() {
        this.prevHash = "";
    }

    public String getPrevHash() {
        return prevHash;
    }


}
