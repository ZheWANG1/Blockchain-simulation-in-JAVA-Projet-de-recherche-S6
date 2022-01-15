import java.security.*;

public class Transaction {
    private final String transaction;
    private final int fromID;
    private final int toID;
    private final double amount;
    private final long timeStamp;
    private final String signature;
    

  public Transaction(String transaction, int fromID, int toID, double amount, long timeStamp, String signature) {
	  this.fromID = fromID;
	  this.toID = toID;
	  this.amount = amount;
	  this.timeStamp = timeStamp;
	  this.signature = signature;
  }
  
  public int getFromID() {
	  return fromID;
  }

  public int getToID() {
	  return toID;
  }


  public double getAmount() {
	  return amount;
  }

  public long getTimeStamp() {
	  return timeStamp;
  }
  
  public String getSignature() {
	  return signature;
  }
  
  public String toString() {
	  return ""+toID+" sent "+ amount+"LD to "+ fromID+" timestamp : "+timeStamp+" signature : " + signature +"\n";
  }
}
