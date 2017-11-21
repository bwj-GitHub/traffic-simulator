package simulator;

import java.util.ArrayList;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ControlListener implements MessageListener {

	public ArrayList<String> messageBuffer;
	
	public ControlListener() {
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
