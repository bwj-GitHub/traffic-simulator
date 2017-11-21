package simulator;

import java.util.ArrayList;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MessageBufferListener implements MessageListener {

	public ArrayList<String> messageBuffer;
	
	public MessageBufferListener() {
		this.messageBuffer = new ArrayList<String>();
	}
	@Override
	public void onMessage(Message m) {
        try {  
        	TextMessage msg = (TextMessage) m;  
      
        	messageBuffer.add(msg.getText());
        } catch (JMSException e) {
        	System.out.println(e);
        }  
	}

}
