/**
 * 
 */
package com.aerohive.a3.amacit.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.aerohive.a3.ama.message.AmaMessage;
import com.aerohive.a3.amacit.util.AmacITUtil;

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
    
    public List<AmaMessage> keepAlive() {
        var httpHeader = AmacITUtil.getAmacHeader();
        
        var httpBody = new HttpEntity<String>("", httpHeader);
        var url = AmacITUtil.getkeepAliveUrl(amacBase, systemId, clusterId);
        var keepAliveRes = restTemplate.exchange(url, 
                HttpMethod.GET, httpBody, String.class);
        
        System.out.println(keepAliveRes);
        return null;
    }
    
    public AmaMessage onBoard(final AmaMessage onboardingMsg) {
        var httpHeader = AmacITUtil.getAmacHeader();
        
        var httpBody = new HttpEntity<AmaMessage>(onboardingMsg, httpHeader);
        var url = AmacITUtil.getOnboardUrl(amacBase, systemId);
        
        var result = restTemplate.exchange(url, HttpMethod.POST, httpBody, AmaMessage.class);
        
        System.out.println(result);
        return result.getBody();
    }
    
    public AmaMessage synReport(final AmaMessage synMessage) {
        var httpHeader = AmacITUtil.getAmacHeader();
        
        var httpBody = new HttpEntity<AmaMessage>(synMessage, httpHeader);
        var url = AmacITUtil.getSynMessageUrl(amacBase, systemId);
        
        var result = restTemplate.exchange(url, HttpMethod.POST, httpBody, AmaMessage.class);
        
        System.out.println(result);
        return result.getBody();
    }
    
    public void reportMessage(final List<AmaMessage> messages) {
        var httpHeader = AmacITUtil.getAmacHeader();
        
        var httpBody = new HttpEntity<List<AmaMessage>>(messages, httpHeader);
        var url = AmacITUtil.getReportUrl(amacBase, systemId);
        
        var result = restTemplate.exchange(url, HttpMethod.POST, httpBody, AmaMessage.class);
        
        System.out.println(result);
    }
}
