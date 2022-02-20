package MessageTypes;

import java.util.ArrayList;
import java.util.List;

public class Message {
    private final String fromAddress;
    private final String toAddress;
    private final long timeStamp;
    private final String signature;
    private final List<Object> messageContent = new ArrayList<>();
    private final int type;
    // Type 0-> Transaction, 1 -> Block

    public Message(String fromAddress, String toAddress, String signature, long timeStamp, int messageType, Object obj){
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.signature = signature;
        this.timeStamp = timeStamp;
        this.type = messageType;
        if( messageType == 1){
            this.messageContent.add( ((List<Object>)obj).get(0));
            this.messageContent.add( ((List<Object>)obj).get(1));
        }else {
            this.messageContent.add(obj);
        }
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getType(){
        return type;
    }

    public String getSignature() {
        return signature;
    }

    public List<Object> getMessageContent(){
        return messageContent;
    }
}
