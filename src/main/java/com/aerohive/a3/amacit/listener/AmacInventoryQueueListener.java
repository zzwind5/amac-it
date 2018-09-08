package com.aerohive.a3.amacit.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aerohive.a3.ama.message.AmaMessage;
import com.aerohive.a3.amacit.common.QueuePublisher;
import com.aerohive.a3.amacit.util.AmaMessageFactory;

@Component
@RabbitListener(queues = "${amac.messaging.queue.event.inventory}")
public class AmacInventoryQueueListener {
    
    @Autowired
    private QueuePublisher publisher;

	@RabbitHandler
    public void process(final AmaMessage message) {
	    var isSuccess = message.getHeader().getHostname().contains("failure") ? false : true;
	    AmaMessage responseMsg = AmaMessageFactory.createResponseMessage(message, isSuccess);
	    
	    publisher.sendToRequestQueue(responseMsg);
    }
}
