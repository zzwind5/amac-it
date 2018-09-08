package com.aerohive.a3.amacit.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aerohive.a3.ama.message.AmaMessage;
import com.aerohive.a3.amacit.common.QueuePublisher;

@Component
@RabbitListener(queues = "${amac.messaging.queue.event.report}")
public class AmacReportingQueueListener {

    @Autowired
    private QueuePublisher publisher;
    
	@RabbitHandler
    public void process(final AmaMessage message) {
	    
    }
}
