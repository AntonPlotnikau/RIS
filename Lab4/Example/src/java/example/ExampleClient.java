/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

/**
 *
 * @author User
 */
public class ExampleClient {

    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory factory;
    
    @Resource(mappedName = "jms/Topic")
    private static Topic topic;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws JMSException {
        String[] strings = {"aaaa", "d", "eeee", "k", "z", "wwww", "bbbb", "ffff", "g", "h"};
        Connection connection = factory.createConnection();
        Session session = connection.createSession(false,  Session.AUTO_ACKNOWLEDGE);
        MessageProducer messageProducer = session.createProducer(topic);
        
        TextMessage message = session.createTextMessage();
        for (int i = 0; i < 10; i++) {
            message.setText(strings[i]);
            System.out.println("Sending message: " +
            message.getText());
            messageProducer.send(message); 
        }
    }  
}
