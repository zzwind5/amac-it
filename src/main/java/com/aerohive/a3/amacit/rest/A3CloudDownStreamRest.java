/**
 * 
 */
package com.aerohive.a3.amacit.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.aerohive.a3.ama.message.AmaMessage;
import com.aerohive.a3.amacit.util.AmacITUtil;

/**
 * @author zjie
 *
 */
@Component
public class A3CloudDownStreamRest {

    @Value("${amac.url}")
    private String amacBase;
    
    @Autowired
    private RestTemplate restTemplate;
    
    public ResponseEntity<AmaMessage[]> pushMessages(final List<AmaMessage> messages) {
        var httpHeader = AmacITUtil.getAmacBasicAuthHeader();
        
        var httpBody = new HttpEntity<List<AmaMessage>>(messages, httpHeader);
        var url = AmacITUtil.getCloutMessagePushUrl(amacBase);
        
        return restTemplate.exchange(url, HttpMethod.POST, httpBody, AmaMessage[].class);
    }
    
    public ResponseEntity<AmaMessage> pushSynMessage(final AmaMessage message) {
        var httpHeader = AmacITUtil.getAmacBasicAuthHeader();
        
        var httpBody = new HttpEntity<AmaMessage>(message, httpHeader);
        var url = AmacITUtil.getCloutMessageSynPushUrl(amacBase);
        
        return restTemplate.exchange(url, HttpMethod.POST, httpBody, AmaMessage.class);
    }
}
