package com.chh.test.mdb;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;

/**
 * Message-Driven Bean implementation class for: TestMDB
 */
@MessageDriven(
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destination", propertyValue = "Queue01"), @ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue")
		}, 
		mappedName = "Queue01")
public class TestMDB implements MessageListener {
	
	/**
     * @see MessageListener#onMessage(Message)
     */
	@Inject JMSContext jmsContext;
	@Resource(mappedName = "Queue02") Queue queue02;
    public void onMessage(Message message) {
    	try {
			String messageBody = message.getBody(String.class);
			System.out.println("MDB received following message body: " + messageBody);
			jmsContext.createProducer().send(queue02, "echo " + messageBody);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
