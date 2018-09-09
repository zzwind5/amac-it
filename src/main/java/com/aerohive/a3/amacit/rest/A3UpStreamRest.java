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
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author zjie
 *
 */
@Component
public class A3UpStreamRest {

    @Value("${amac.url}")
    private String amacBase;
    
    @Value("${a3.systemId}")
    private String systemId;
    
    @Value("${a3.clusterId}")
    private String clusterId;
    
    @Autowired
    private RestTemplate restTemplate;
    
    public ResponseEntity<AmaMessage[]> keepAlive() {
        var httpHeader = AmacITUtil.getAmacHeader();
        var httpBody = new HttpEntity<String>("", httpHeader);
        var url = AmacITUtil.getkeepAliveUrl(amacBase, systemId, clusterId);
        
        var keepAliveRes = restTemplate.exchange(url, HttpMethod.GET, httpBody, AmaMessage[].class);
        
        return keepAliveRes;
    }
    
    public ResponseEntity<AmaMessage> onBoard(final AmaMessage onBoardMsg) {
        var httpHeader = AmacITUtil.getAmacHeader();
        
        var httpBody = new HttpEntity<AmaMessage>(onBoardMsg, httpHeader);
        var url = AmacITUtil.getOnboardUrl(amacBase, systemId);
        
        var result = restTemplate.exchange(url, HttpMethod.POST, httpBody, AmaMessage.class);
        
        return result;
    }
    
    public ResponseEntity<AmaMessage> synReport(final AmaMessage synMessage) {
        var httpHeader = AmacITUtil.getAmacHeader();
        
        var httpBody = new HttpEntity<AmaMessage>(synMessage, httpHeader);
        var url = AmacITUtil.getSynMessageUrl(amacBase, systemId);
        
        return restTemplate.exchange(url, HttpMethod.POST, httpBody, AmaMessage.class);
    }
    
    public ResponseEntity<String> reportMessage(final List<AmaMessage> messages) {
        var httpHeader = AmacITUtil.getAmacHeader();
        
        var httpBody = new HttpEntity<List<AmaMessage>>(messages, httpHeader);
        var url = AmacITUtil.getReportUrl(amacBase, systemId);
        
        return restTemplate.exchange(url, HttpMethod.POST, httpBody, String.class);
    }
}
