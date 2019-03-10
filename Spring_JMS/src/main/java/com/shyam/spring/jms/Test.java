package com.shyam.spring.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class Test {

	public static void main(String args[]) {
		try {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("com/shyam/spring/jms/context.xml");

		
		JmsTemplate jmsTemplate = (JmsTemplate) applicationContext.getBean("jmsTemplate");
		
		//this will send the message to the default queue
		jmsTemplate.send(new MessageCreator() {
			
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				TextMessage tm = session.createTextMessage("this is from spring config");
				return tm;
			}
		});
		
		System.out.println("message sent!!");
		Thread.sleep(1000);
		
		TextMessage textMessage = (TextMessage)jmsTemplate.receive();
		System.out.println("Message received is: " + textMessage.getText());
		
		//this will send the message to the provided queue
		jmsTemplate.send("HelloWorld.TestQ", new MessageCreator() {
			
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				TextMessage tm = session.createTextMessage("This message is for different query name: " + "HelloWorld.TestQ");
				return tm;
			}
		});
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
}
