package com.shyam.core.thread.jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class HelloWorldConsumerThread implements Runnable, ExceptionListener{
	
	public void run() {
		try {
		//creating the connection factory
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://Shyam-PC:61616");
		
		//get the connection from the connection factory
		Connection connection = activeMQConnectionFactory.createConnection();
		connection.start();
		
		//create the session from connection
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//create destination
		Destination destination = session.createQueue("HelloWorld.TestQ");
		
		//Create a message consumer from the session for the queue/topic
		MessageConsumer messageConsumer = session.createConsumer(destination);
		
		//Wait for message
		Message message = messageConsumer.receive(1000);
		
		if(message instanceof TextMessage) {
			TextMessage txtMessage = (TextMessage) message;
			System.out.println("Message Received: " + txtMessage.getText());
			System.out.println();
		}else {
			System.out.println("Message Received: " + message);
		}
		messageConsumer.close();
		session.close();
		connection.close();
		
		}catch(JMSException ex) {
			ex.printStackTrace();
		}
	}

	public void onException(JMSException exception) {
		// TODO Auto-generated method stub
		System.out.println("JMS exception occurred. Shutting down client.");
	}
}
