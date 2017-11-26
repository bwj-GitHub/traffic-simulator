package events.distributed;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;

import events.CarUpdateEvent;
import events.Event;
import events.EventQueue;
import simulator.Config;


/**
 * A queue for storing Events sent from a single, specific non-local node; also 
 * facilitates communication with that node.
 * @author brandon
 *
 */
public class ExternalEventQueue extends EventQueue{

	Config config;
	EventMessageListener messageListener;
	QueueSession queueSession;   // for reading incoming messages
	QueueReceiver queueReceiver;
	QueueSender queueSender;
	EventQueue queue;
	Event lastSentEvent;

	public ExternalEventQueue(Config config, QueueSession queueSession, Queue inputQueue,
			Queue outputQueue) {
		queue = new EventQueue();
		messageListener = null;
		lastSentEvent = null;

		// Connect to queue of incoming messages and set listener:
		this.queueSession = queueSession;
		try {
			queueReceiver = queueSession.createReceiver(inputQueue);
			messageListener = new EventMessageListener();
			queueReceiver.setMessageListener(messageListener);
		} catch (JMSException e) {
			e.printStackTrace();
		}

		// Connect to queue for outgoing messages:
		try {
			queueSender = queueSession.createSender(outputQueue);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void add(Event event) {
		queue.add(event);
	}

	public void add(Event[] events) {
		for (Event event: events) {
			add(event);
		}
	}

	/**
	 * If event is in this.queue, remove it; otherwise, do nothing.
	 * 
	 * Use of this method is discouraged, as insertion into an EventQueue can
	 * be fairly expensive; the user should do their best to 
	 */
	public void remove(Event event) {
		queue.remove(event);
	}

	public void sendEventMessage(Event event) {
		String messageText = event.getStringRepresentation();
		try {
			TextMessage message = queueSession.createTextMessage();
			message.setText(messageText);
			queueSender.send(message);
			lastSentEvent = event;
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public int getSize() {
		parseBufferedMessages();
		return this.queue.getSize();
	}

	public Event peek() {
		parseBufferedMessages();
		Event nextEvent = null;
		do {
			nextEvent = queue.peek();
			if (nextEvent == null) {
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} while (nextEvent == null);
		return nextEvent;
	}

	public Event poll() {
		parseBufferedMessages();
		Event nextEvent = null;
		do {
			nextEvent = queue.poll();
			if (nextEvent == null) {
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} while (nextEvent == null);
		return nextEvent;
	}

	/**
	 * Instantiate Events from the messages in messageListener.messageBuffer; the
	 * messageBuffer is cleared after doing this.
	 */
	private void parseBufferedMessages() {
		int numMessages = messageListener.messageBuffer.size();
		for (int i = 0; i < numMessages; i++) {
			Event event = parseEventMessage(messageListener.messageBuffer.remove(0));
			queue.add(event);
		}
	}

	/**
	 * Instantiate an Event using the text of a TextMessage.
	 * @param eventMessage: a String created from a TextMessage.
	 * @return an Event.
	 */
	private Event parseEventMessage(String eventMessage) {
		String[] parts = eventMessage.split("\t", 2);
		if (parts[0] == "CarUpdateEvent") {
			return new CarUpdateEvent(eventMessage);
		} else {
			// TODO: What other types of Events can be passed?
			return null;
		}
	}
}
