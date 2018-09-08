package com.aerohive.a3.amacit.common;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aerohive.a3.ama.message.AmaMessage;

@Component
public class QueuePublisher {

	@Autowired
	private AmqpTemplate amqpTemplate;
	
	
	@Value("${amac.messaging.queue.request}")
	private String inBoundRequestQueueName;
	
	public void sendToRequestQueue(final AmaMessage message) {
		this.send(message,  inBoundRequestQueueName, null);
	}
	
	private void send(final Object message, final String queueName, final String exchange) {
        try {
            if (StringUtils.isEmpty(exchange) == false) {
            	 amqpTemplate.convertAndSend(exchange, queueName, message);
            } else {
            	amqpTemplate.convertAndSend(queueName, message);
            }
            
        }
        catch (AmqpException ae) {
            throw ae;
        }
    }
}
