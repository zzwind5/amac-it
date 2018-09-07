/**
 * 
 */
package com.aerohive.a3.amacit.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * @author zjie
 *
 */
public class AmacITUtil {
    
    public static HttpHeaders getAmacHeader() {
        return getJsonHeader(AmacITContainer.amacToken);
    }
    
    public static HttpHeaders getAmacBasicAuthHeader() {
        HttpHeaders header = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json");
        header.setContentType(type);
        header.set( "Authorization", String.format("Basic YWRtaW46QWVyb2hpdmUxMjM=") );
        
        return header;
    }
    
    public static HttpHeaders getGdcHeader() {
        return getJsonHeader(AmacITContainer.gdcToken);
    }

    public static HttpHeaders getJsonHeader(final String token) {
        HttpHeaders header = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json");
        header.setContentType(type);
        header.set("Authorization", String.format("Bearer %s", token) );
        return header;
    }
    
    public static String getGdcUrl(final String gdcBase) {
        return gdcBase + "/oauth/cookietoken";
    }

    public static String getVhmBinding(final String gdcBase) {
        return gdcBase + "/services/acct/selectvhm";
    }
    
    public static String getTokenSynUrl(String hmUrl) {
        return hmUrl + "/security/csrftoken";
    }
    
    public static String getAmacTokenUrl(final String amacBase, final String systemId) {
        return amacBase + "/amac/rest/token/apply/" + systemId;
    }
    
    public static String getkeepAliveUrl(final String amacBase, final String systemId, final String clusterId) {
        return String.format("%s/amac/rest/v1/poll/%s?clusterId=%s", amacBase, systemId, clusterId);
    }
    
    public static String getOnboardUrl(final String amacBase, final String systemId) {
        return String.format("%s/amac/rest/v1/onboarding/%s", amacBase, systemId);
    }
    
    public static String getSynMessageUrl(final String amacBase, final String systemId) {
        return String.format("%s/amac/rest/v1/report/syn/%s", amacBase, systemId);
    }
    
    public static String getReportUrl(final String amacBase, final String systemId) {
        return String.format("%s/amac/rest/v1/report/%s", amacBase, systemId);
    }
    
    public static String getCloutMessagePushUrl(final String amacBase) {
        return String.format("%s/amac/rest/message/push/asyn", amacBase);
    }
}
