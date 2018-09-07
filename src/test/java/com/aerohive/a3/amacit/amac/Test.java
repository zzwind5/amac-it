/**
 * 
 */
package com.aerohive.a3.amacit.amac;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zjie
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {

    @Value("${gdc.auth.url}")
    private String gdcUrl;
    
    @Value("${amac.url=}")
    private String amacUrl;
    
    @Value("${gdc.auth.username}")
    private String gdcUserName;
    
    @Value("${gdc.auth.password}")
    private String gdcPassword;

//    @Test
//    public void GdcToken() {
//        
//    }
}
