package com.shyam.jms.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

public class BasicJMSChat implements MessageListener {

	private JmsTemplate chatJMSTemplate;
	private Topic chatTopic;
	private static String userId;

	public static void main(String args[]) throws JMSException, IOException {

		if (args.length != 1) {
			System.out.println("Username is required!!!");
		} else {
			userId = args[0];

			ApplicationContext appContext = new ClassPathXmlApplicationContext("com/shyam/jms/chat/spring-context.xml");

			BasicJMSChat basicJMSChat = (BasicJMSChat) appContext.getBean("basicJMSChat");

			TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) basicJMSChat.chatJMSTemplate
					.getConnectionFactory();
			TopicConnection topicConnection = topicConnectionFactory.createTopicConnection();

			basicJMSChat.publish(topicConnection, basicJMSChat.chatTopic, userId);
			basicJMSChat.subscribe(topicConnection, basicJMSChat.chatTopic, basicJMSChat);
		}
	}

	public void publish(TopicConnection tc, Topic topic, String userId) throws JMSException, IOException {

		TopicSession topicSession = tc.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

		TopicPublisher topicPublisher = topicSession.createPublisher(topic);
		tc.start();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String msgToSend = br.readLine();
			if (msgToSend.equalsIgnoreCase("exit")) {
				tc.close();
				System.exit(0);
			} else {
				TextMessage tm = topicSession.createTextMessage();
				tm.setText("\n[" + userId + "] --> " + msgToSend);
				topicPublisher.publish(tm);
			}
		}
	}

	public void subscribe(TopicConnection tc, Topic topic, BasicJMSChat commandLineChat) throws JMSException {

		TopicSession topicSession = tc.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);
		topicSubscriber.setMessageListener(commandLineChat);
	}

	public void onMessage(Message message) {

		if (message instanceof TextMessage) {
			try {
				String msgText = ((TextMessage) message).getText();
				if (!msgText.startsWith("[" + userId)) {
					System.out.println(msgText);
				}
			} catch (JMSException ex) {
				String errorMsg = "Error occurred while extracting the message";
				ex.printStackTrace();
				throw new RuntimeException(errorMsg);
			}
		} else {
			String errorMsg = "Message is not expected type TextMessage";
			throw new RuntimeException(errorMsg);
		}

	}

	public JmsTemplate getChatJMSTemplate() {
		return chatJMSTemplate;
	}

	public void setChatJMSTemplate(JmsTemplate chatJMSTemplate) {
		this.chatJMSTemplate = chatJMSTemplate;
	}

	public Topic getChatTopic() {
		return chatTopic;
	}

	public void setChatTopic(Topic chatTopic) {
		this.chatTopic = chatTopic;
	}

	public static String getUserId() {
		return userId;
	}

	public static void setUserId(String userId) {
		BasicJMSChat.userId = userId;
	}
}
