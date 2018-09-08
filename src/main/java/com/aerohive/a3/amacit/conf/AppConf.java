/**
 * 
 */
package com.aerohive.a3.amacit.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.datatype.guava.GuavaModule;

/**
 * @author zjie
 *
 */
@Configuration
public class AppConf {

    @Bean
    public RestTemplate httpsRestTemplate() {
        return new RestTemplate(new HttpsClientRequestFactory());
    }
    
    @Bean
    public GuavaModule guavaModule() {
        return new GuavaModule();
    }
}
