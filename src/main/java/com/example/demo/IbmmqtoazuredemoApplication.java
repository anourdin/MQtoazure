package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;

@SpringBootApplication
@RestController
@EnableJms
public class IbmmqtoazuredemoApplication {

	@Autowired
	private JmsTemplate jmsTemplate;

	 public static void main(String[] args) {
		SpringApplication.run(IbmmqtoazuredemoApplication.class, args);
	}

	@JmsListener(destination = "azuredemoin" )
	@SendTo("azurestatusq")
	String recv(){
	    try{
	    	String connectionString = "Endpoint=sb://apicentrics.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=f0j9monM6TRW+QUS+709zVqWcjAB8zaFpbAR/Tk2uJc=";
		    String azureQueueName = "indy-in";
		    String message =  jmsTemplate.receiveAndConvert("azuredemoin").toString();
	    	sendMessage(connectionString,azureQueueName,message);	
	    	return "Success";
	    	
	    }catch(JmsException ex){
	        ex.printStackTrace();
	       return  "fail";	
	      }
	}
	
	public void sendMessage(String connectionString, String queueName, String message)
	{
		
		ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
	            .connectionString(connectionString)
	            .sender()
	            .queueName(queueName)
	            .buildClient();

	    senderClient.sendMessage(new ServiceBusMessage(message));
	    System.out.println("Sent a message to the queue: " + queueName);        
	}


}
