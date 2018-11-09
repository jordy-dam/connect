package sample.activemq;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.softwareag.util.IDataMap;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import javax.jms.*;
// --- <<IS-END-IMPORTS>> ---

public final class client

{
	// ---( internal utility methods )---

	final static client _instance = new client();

	static client _newInstance() { return new client(); }

	static client _cast(Object o) { return (client)o; }

	// ---( server methods )---




	public static final void connect (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(connect)>> ---
		// @sigtype java 3.5
    final ActiveMQConnectionFactory connectionFactory = createActiveMQConnectionFactory();
    final PooledConnectionFactory pooledConnectionFactory = createPooledConnectionFactory(connectionFactory);

    IDataMap map = new IDataMap(pipeline);
    
    try {
		map.put("producer", sendMessage(pooledConnectionFactory));
		//map.put("subscriber", receiveMessage(connectionFactory));
		
	} catch (JMSException e) {
		// TODO Auto-generated catch block
		throw new ServiceException(e.toString());
	}

    pooledConnectionFactory.stop();
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	static String sendMessage(PooledConnectionFactory pooledConnectionFactory) throws JMSException {
	    // Establish a connection for the producer.
	    final Connection producerConnection = pooledConnectionFactory.createConnection();
	    producerConnection.start();
	
	    // Create a session.
	    final Session producerSession = producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	
	    // Create a queue named "MyQueue".
	    final Destination producerDestination = producerSession.createQueue("MyQueue");
	
	    // Create a producer from the session to the queue.
	    final MessageProducer producer = producerSession.createProducer(producerDestination);
	    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
	
	    // Create a message.
	    final String text = "Hello from inQdo HQ!";
	    final TextMessage producerMessage = producerSession.createTextMessage(text);
	
	    // Send the message.
	    producer.send(producerMessage);
	
	    // Clean up the producer.
	    producer.close();
	    producerSession.close();
	    producerConnection.close();
	    
	    // Set output
	    return "Message sent.";
	}
	
	static String receiveMessage(ActiveMQConnectionFactory connectionFactory) throws JMSException {
	    // Establish a connection for the consumer.
	    // Note: Consumers should not use PooledConnectionFactory.
	    final Connection consumerConnection = connectionFactory.createConnection();
	    consumerConnection.start();
	
	    // Create a session.
	    final Session consumerSession = consumerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	
	    // Create a queue named "MyQueue".
	    final Destination consumerDestination = consumerSession.createQueue("MyQueue");
	
	    // Create a message consumer from the session to the queue.
	    final MessageConsumer consumer = consumerSession.createConsumer(consumerDestination);
	
	    // Begin to wait for messages.
	    final Message consumerMessage = consumer.receive(1000);
	
	    // Receive the message when it arrives.
	    final TextMessage consumerTextMessage = (TextMessage) consumerMessage;
	
	    // Clean up the consumer.
	    consumer.close();
	    consumerSession.close();
	    consumerConnection.close();
	    
	 // Set output
	    return "Message received: "+consumerTextMessage.getText();
	}
	
	static PooledConnectionFactory createPooledConnectionFactory(ActiveMQConnectionFactory connectionFactory) {
	    // Create a pooled connection factory.
	    final PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
	    pooledConnectionFactory.setConnectionFactory(connectionFactory);
	    pooledConnectionFactory.setMaxConnections(10);
	    return pooledConnectionFactory;
	}
	
	static ActiveMQConnectionFactory createActiveMQConnectionFactory() {
	    
	    // Specify the connection parameters.
	    final String WIRE_LEVEL_ENDPOINT = "ssl://b-91d33f00-2ee6-4a20-9954-86baa9531f6a-1.mq.eu-west-1.amazonaws.com:61617";
	    final String ACTIVE_MQ_USERNAME = "connect";
	    final String ACTIVE_MQ_PASSWORD = "inqdoconnect";
	    
		// Create a connection factory.
	    final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(WIRE_LEVEL_ENDPOINT);
	
	    // Pass the username and password.
	    connectionFactory.setUserName(ACTIVE_MQ_USERNAME);
	    connectionFactory.setPassword(ACTIVE_MQ_PASSWORD);
	    return connectionFactory;
	}
	// --- <<IS-END-SHARED>> ---
}

