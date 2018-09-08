package com.aerohive.a3.amacit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpServerErrorException;

import com.aerohive.a3.ama.message.common.AmaResponseMessageData;
import com.aerohive.a3.amacit.rest.A3CloudDownStreamRest;
import com.aerohive.a3.amacit.rest.A3UpStreamRest;
import com.aerohive.a3.amacit.rest.AmacTokenRest;
import com.aerohive.a3.amacit.rest.GdcTokenRest;
import com.aerohive.a3.amacit.util.AmaMessageFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AmacItApplicationTests {
    
    @Value("${a3.systemId}")
    private String systemId;
    
    @Autowired
    private GdcTokenRest gdcRest;
    
    @Autowired
    private AmacTokenRest amacRest;
    @Autowired
    private A3UpStreamRest upStreamRests;
    @Autowired
    private A3CloudDownStreamRest downStreamRests;
    
    @Before
    public void init() {
        System.out.println("GDC Token: " + gdcRest.getGdcToken());
        System.out.println("AMAC Token: " + amacRest.getAmacToken());
    }

	@Test
	public void noBoardSuccess() {
	    //On boarding
	    var onBoardMsg = AmaMessageFactory.createOnBoardMessage(systemId);
	    var onBoardResult = upStreamRests.onBoard(onBoardMsg);
	    assertThat(onBoardResult.getStatusCode()).isEqualTo(HttpStatus.OK);
	    assertThat(onBoardResult.getBody().getData()).isInstanceOf(AmaResponseMessageData.class);
	    assertThat( ((AmaResponseMessageData)onBoardResult.getBody().getData()).isSuccessful() ).isTrue();
	    
	    //Keep alive
	    var keepResult = upStreamRests.keepAlive();
	    assertThat(keepResult.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test(expected= HttpServerErrorException.class) 
    public void noBoardFailed() {
        //On boarding
        var onBoardMsg = AmaMessageFactory.createOnBoardMessage(systemId);
        onBoardMsg.getHeader().setHostname("failure");
        var onBoardResult = upStreamRests.onBoard(onBoardMsg);
        assertThat(onBoardResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(onBoardResult.getBody().getData()).isInstanceOf(AmaResponseMessageData.class);
        assertThat( ((AmaResponseMessageData)onBoardResult.getBody().getData()).isSuccessful() ).isFalse();
        
        //Keep alive
        upStreamRests.keepAlive();
    }
	
	@Test(expected= HttpServerErrorException.class) 
    public void withOutOnBoard() {
	    //Keep alive
	    upStreamRests.keepAlive();
    }
	
	
	@After
    public void destroy() {
	    downStreamRests.pushMessages(
	            Collections.singletonList(AmaMessageFactory.createClearMessage(systemId))
	    );
    }
}
