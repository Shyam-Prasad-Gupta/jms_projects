package com.shyam.core.jms;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class HelloWorldProducer {
	
	public static void main(String args[]) {
		
		try {
		//create a connection factory
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://Shyam-PC:61616");
		
		//get a connection from connection factory
		Connection connection = activeMQConnectionFactory.createConnection();
		connection.start();
		
		//get a session fromt the connection
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//create a destination queue from this session
		Destination destination = session.createQueue("HelloWorld.TestQ");
		
		//create a jms producer from this session
		MessageProducer messageProducer = session.createProducer(destination);
		messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		
		//create message
		String message = "Hello world greeting! from: " + Thread.currentThread().getName();
		TextMessage textMessage = session.createTextMessage(message);
		
		System.out.println("Sent Message: " + textMessage.hashCode() + Thread.currentThread().getName());
		
		messageProducer.send(textMessage);
		
		//clean up
		messageProducer.close();
		session.close();
		connection.close();
		
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
