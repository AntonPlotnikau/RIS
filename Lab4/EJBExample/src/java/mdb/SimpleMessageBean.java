/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author User
 */
@JMSDestinationDefinition(name = "jms/Topic", interfaceName = "javax.jms.Topic", resourceAdapter = "jmsra", destinationName = "Topic")
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/Topic"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class SimpleMessageBean implements MessageListener {
    
    static final Logger logger = Logger.getLogger("SimpleMessageBean");
    
    private List<String> sortedStrings = new ArrayList();
    
    @Resource
    private MessageDrivenContext mdc;
    
    public SimpleMessageBean() {
    }
    
    @Override
    public void onMessage(Message inMessage) {
        TextMessage msg = null;

        try {
            if (inMessage instanceof TextMessage) {
                msg = (TextMessage) inMessage;
                sortedStrings.add(msg.getText());
            } else {
                logger.warning("Message of wrong type: " + inMessage.getClass().getName());
            }
        } catch (JMSException e) {
             e.printStackTrace();
            mdc.setRollbackOnly();
        } catch (Throwable te) {
            te.printStackTrace();
        }
        
        if(sortedStrings.size() == 10){
            Collections.sort(sortedStrings); 
        
            for(String str: sortedStrings){
                if(str.length() >= 4)
                    logger.info(str);
            }
        }
        
    }  
}
