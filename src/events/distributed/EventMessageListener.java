package events.distributed;

import java.util.ArrayList;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class EventMessageListener implements MessageListener {

	public ArrayList<String> messageBuffer;

	public EventMessageListener() {
		messageBuffer = new ArrayList<String>();
	}

	@Override
	public void onMessage(Message m) {
        try {  
        TextMessage msg = (TextMessage) m;  
      
        messageBuffer.add(msg.getText());
        } catch(JMSException e) {
        	e.printStackTrace();
        } 
	}
}
