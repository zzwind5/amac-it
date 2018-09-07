package com.aerohive.a3.amacit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.aerohive.a3.amacit.rest.A3UpStreamRest;
import com.aerohive.a3.amacit.rest.AmacTokenRest;
import com.aerohive.a3.amacit.rest.GdcTokenRest;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AmacItApplicationTests {
    
    @Autowired
    private GdcTokenRest gdcRest;
    
    @Autowired
    private AmacTokenRest amacRest;
    @Autowired
    private A3UpStreamRest upStreamRests;

	@Test
	public void contextLoads() {
	    System.out.println("GDC Token: " + gdcRest.getGdcToken());
	    System.out.println("AMAC Token: " + amacRest.getAmacToken());
	    System.out.println("Keep Alive: " + upStreamRests.keepAlive());
	}
	
	
}
