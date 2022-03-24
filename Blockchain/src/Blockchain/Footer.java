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

// Final Hash 1 (Hash blockchain physic)
// Final Hash 2 (Hash de la blockchain de type block.type)

// B1-B2-B3-B1(2)
// B1  [Header : Hash footer de B1 (Previous block hash of type 1), Previous hash of previous block(Physique)]
// [Body] Aucun changement
// [Footer] : Final Hash Type1 -> Hash(Body+Header.HashFooterdeB1)
// Final Hash physic -> Hash(Body+Header.previousBlock)