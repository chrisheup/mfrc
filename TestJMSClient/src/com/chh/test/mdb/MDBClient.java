package com.chh.test.mdb;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;

public class MDBClient implements MessageListener {

	public static void main(String[] args) throws JMSException, NamingException, InterruptedException {
		MDBClient mdbClient = new MDBClient();
		Context context = getInitialContext();
		Queue queue01 = (Queue) context.lookup("Queue01");
		Queue queue02 = (Queue) context.lookup("Queue02");
		
		JMSContext jmsContext = ((ConnectionFactory) context.lookup("GFConnectionFactory")).createContext();
		jmsContext.createConsumer(queue02).setMessageListener(mdbClient);
		
		JMSProducer jmsProducer = jmsContext.createProducer();
		String testString = "testString";
		
		for (int i = 0; i < 10; i++) {
			jmsProducer.send(queue01, testString + ":" + i); 
			Thread.sleep(100);
		}
	}

	@Override
	public void onMessage(Message message) {
		try {
			String messageBody = message.getBody(String.class);
			System.out.println("Client received following message body: " + messageBody);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public static Context getInitialContext() throws JMSException, NamingException {
		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
		props.setProperty("java.naming.provider.url", "iiop://localhost:3700");
		return new InitialContext(props);
	}
}
