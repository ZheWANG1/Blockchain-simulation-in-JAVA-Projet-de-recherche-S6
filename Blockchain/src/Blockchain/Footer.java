package Blockchain;

public class Footer {

    private String hash;

    public Footer(){this.hash = "";}

    public Footer(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String toString(){
        return "\nFooter hash : " + hash;
    }
}

