import java.security.*;

public class Transaction {
    private final int fromID;
    private final int toID;
    private final double amount;
    private final long timeStamp;
    private String signature;
    private final int transactionID;
    private static int cpt =0;
    private static final Object o = new Object();
    

  public Transaction(String transaction, int fromID, int toID, double amount, long timeStamp, PrivateKey pv) {
	  synchronized(o) {
		  transactionID = cpt++;
	  }
	  this.fromID = fromID;
	  this.toID = toID;
	  this.amount = amount;
	  this.timeStamp = timeStamp;
	  try {
		this.signature = RsaUtil.sign(this.toString(), pv);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		this.signature = null;
	}
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
