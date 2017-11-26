package simulator;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TwoWayMessenger {

	Properties connectionProps;
	QueueConnection connection;
	QueueSession queueSession;
    QueueReceiver queueReceiver;
	MessageBufferListener messageListener;
    QueueSender queueSender;
    String factoryName;
    String providerURL;

	public TwoWayMessenger(String inputQueueName, String outputQueueName,
			String factoryName, String providerURL) {
		// Create Properties for connections
        connectionProps = createProperties(providerURL);
        connection = null;
        queueSession = null;
        queueReceiver = null;
        messageListener = null;
        queueSender = null;
		messageListener = new MessageBufferListener();
        this.factoryName = factoryName;
        this.providerURL = providerURL;

		// Create Connections and Queues:
		try {
			// Lookup ConnectionFactory by name and create and start a connection:
			InitialContext context = new InitialContext(connectionProps);

			// NOTE: This will block until the factory is found; also, this could
			//	take some time (a few seconds)
			System.out.println("Waiting on ConnectionFactory...");
			QueueConnectionFactory connectionFactory =
					(QueueConnectionFactory) context.lookup(factoryName);
			connection = connectionFactory.createQueueConnection();
			connection.start();
			queueSession = connection.createQueueSession(false,
					Session.AUTO_ACKNOWLEDGE);

			// Create receiver:
			System.out.println("Waiting on QueueReceiver...");
			Queue receiverQueue = (Queue) context.lookup(inputQueueName);
			queueReceiver = queueSession.createReceiver(receiverQueue);
			queueReceiver.setMessageListener(messageListener);

			// Create sender:
			System.out.println("Waiting on QueueSender...");
			Queue senderQueue = (Queue) context.lookup(outputQueueName);
			queueSender = queueSession.createSender(senderQueue);

		} catch (NamingException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (JMSException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

	public void send(String message) {
		try {
			TextMessage messageObj = queueSession.createTextMessage();
			messageObj.setText(message);
			queueSender.send(messageObj);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			this.connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public int getMessageBufferSize() {
		return this.messageListener.messageBuffer.size();
	}
	
	public String getNextMessage() {
		return this.messageListener.messageBuffer.remove(0);
	}

	private Properties createProperties(String providerURL) {
		Properties connectionProps = new Properties();
		connectionProps.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.enterprise.naming.SerialInitContextFactory");
		connectionProps.put(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
		connectionProps.put(Context.PROVIDER_URL, providerURL);
		return connectionProps;
	}
}
