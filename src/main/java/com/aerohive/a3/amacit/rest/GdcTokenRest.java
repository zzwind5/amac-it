/**
 * 
 */
package com.aerohive.a3.amacit.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.aerohive.a3.amacit.model.GdcTokenResult;
import com.aerohive.a3.amacit.model.VhmBindResult;
import com.aerohive.a3.amacit.util.AmacITContainer;
import com.aerohive.a3.amacit.util.AmacITUtil;

/**
 * @author zjie
 *
 */
@Component
public class GdcTokenRest {

    @Value("${gdc.auth.url}")
    private String gdcUrlBase;
    
    @Value("${gdc.auth.username}")
    private String gdcUserName;
    
    @Value("${gdc.auth.password}")
    private String gdcPassword;
    
    @Autowired
    private RestTemplate restTemplate;
    
    public String getGdcToken() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded");
        headers.setContentType(type);
        String bodyStr = String.format("grant_type=password&client_id=browser&client_secret=secret&username=%s&password=%s", gdcUserName, gdcPassword);
        
        var formEntity = new HttpEntity<String>(bodyStr, headers);
        GdcTokenResult result = restTemplate.postForObject(AmacITUtil.getGdcUrl(gdcUrlBase), formEntity, GdcTokenResult.class);
        
        var token = result.getAccessToken();
        
        var vhmBinding = new HttpEntity<String>("", AmacITUtil.getJsonHeader(token) );
        var vhmResult = restTemplate.exchange(AmacITUtil.getVhmBinding(gdcUrlBase), HttpMethod.GET, vhmBinding, VhmBindResult.class);
        var hmUrl = vhmResult.getBody().getData().getLocation();
        
        var tokenSyn = new HttpEntity<String>("", AmacITUtil.getJsonHeader(token) );
        restTemplate.exchange( AmacITUtil.getTokenSynUrl(hmUrl), HttpMethod.GET, tokenSyn, String.class);
        
        AmacITContainer.gdcToken = token;
        return token;
    }
}
