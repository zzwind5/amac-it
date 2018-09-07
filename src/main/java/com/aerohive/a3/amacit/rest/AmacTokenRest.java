/**
 * 
 */
package com.aerohive.a3.amacit.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.aerohive.a3.ama.message.AmaMessage;
import com.aerohive.a3.ama.message.AmaMessageHeader;
import com.aerohive.a3.ama.message.common.AmacTokenMessageData;
import com.aerohive.a3.amacit.util.AmacITContainer;
import com.aerohive.a3.amacit.util.AmacITUtil;

/**
 * @author zjie
 *
 */
@Component
public class AmacTokenRest {

    @Value("${amac.url}")
    private String amacBase;
    
    @Value("${a3.hostname}")
    private String hostname;
    
    @Value("${a3.systemId}")
    private String systemId;
    
    @Value("${a3.clusterId}")
    private String clusterId;
    
    @Autowired
    private RestTemplate restTemplate;
    
    public String getAmacToken() {
        
        var httpHeader = AmacITUtil.getJsonHeader(AmacITContainer.gdcToken);
        var msgHeader = new AmaMessageHeader(systemId, clusterId, hostname);
        
        var httpBody = new HttpEntity<AmaMessage>(new AmaMessage(msgHeader, null), httpHeader);
        var vhmResult = restTemplate.exchange(AmacITUtil.getAmacTokenUrl(amacBase, systemId), 
                HttpMethod.POST, httpBody, AmaMessage.class);
        
        var amacToken = ( (AmacTokenMessageData)vhmResult.getBody().getData() ).getToken();
        
        AmacITContainer.amacToken = amacToken;
        return amacToken;
    }
}
