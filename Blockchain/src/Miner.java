import java.util.List;

public class Miner extends Node {

    public Miner(String name, Network network) {
        super(name, network);
    }

    public void receiptTransaction(Transaction transaction){}
    // Le mineur doit ajouter la transaction qui lui fais gagner de l'argent dans le block avant de miner
    // Le mineur gagnant broadcastera le block avec la transaction de son gain sans verification de la signature (Pour simplifier)
    // Le Network pendant le broadcast fera alors un update des wallets via la fonction : updateAllWallet
    // Donc le miner gagnant aura juste a broadcaster son Block et les wallets seront update
    
}
