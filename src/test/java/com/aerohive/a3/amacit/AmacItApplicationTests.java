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

import com.aerohive.a3.ama.message.AmaMessage;
import com.aerohive.a3.ama.message.AmaMessageHeader;
import com.aerohive.a3.ama.message.common.AmaResponseMessageData;
import com.aerohive.a3.ama.message.inventory.AmaHelloInventoryTestMessageData;
import com.aerohive.a3.amacit.rest.A3CloudDownStreamRest;
import com.aerohive.a3.amacit.rest.A3UpStreamRest;
import com.aerohive.a3.amacit.rest.AmacTokenRest;
import com.aerohive.a3.amacit.rest.GdcTokenRest;
import com.aerohive.a3.amacit.util.AmaMessageFactory;
import com.aerohive.a3.amacit.util.AmacITUtil;

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
	
	@Test
    public void a3AsynMessageReport() {
        //Onboarding
	    noBoardSuccess();
	    
	    //Sender Hello Inventory Message
	    var header = new AmaMessageHeader(systemId, null, null);
	    var helloInventory = new AmaHelloInventoryTestMessageData("Hello A3 Inventory!");
	    var inventoryRes = upStreamRests.reportMessage( Collections.singletonList(new AmaMessage(header, helloInventory)) );
	    assertThat(inventoryRes.getStatusCode()).isEqualTo(HttpStatus.OK);
	    
	    var helloReport = new AmaHelloInventoryTestMessageData("Hello A3 Report!");
        var reportRes = upStreamRests.reportMessage( Collections.singletonList(new AmaMessage(header, helloReport)) );
        assertThat(reportRes.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
	
	@Test
    public void a3SynMessageReport() {
        //Onboarding
        noBoardSuccess();
        
        var header = new AmaMessageHeader(systemId, null, null);
        var messageId = header.getMessageId();
        
//        //Push response delay 1s.
//        AmacITUtil.executeDelay( () -> {
//            var headerResp = new AmaMessageHeader(systemId, null, null);
//            var headerRespBody = new AmaResponseMessageData(messageId+"_syn", false, 0, "Hello A3 !", null, null);
//            downStreamRests.pushMessages( Collections.singletonList(new AmaMessage(headerResp, headerRespBody)) );
//        }, 1000);
        
        //Sender Hello Inventory Message
        var helloInventory = new AmaHelloInventoryTestMessageData("Hello A3 Inventory!");
        var result = upStreamRests.synReport( new AmaMessage(header, helloInventory) );
        
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getData()).isInstanceOf(AmaResponseMessageData.class);
        assertThat( ((AmaResponseMessageData)result.getBody().getData()).isSuccessful() ).isTrue();
    }
	
	@Test
    public void a3CloudPushMessage() {
        //Onboarding
        noBoardSuccess();
        
        //Sender Hello Inventory Message
        var header = new AmaMessageHeader(systemId, null, null);
        var helloInventory = new AmaHelloInventoryTestMessageData("Hello A3!");
        var result = downStreamRests.pushMessages( Collections.singletonList( new AmaMessage(header, helloInventory) ));
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        //keep alive poll.
        var pollRes = upStreamRests.keepAlive();
        assertThat(pollRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(pollRes.getBody()).hasSize(1);
        assertThat(pollRes.getBody()[0].getData()).isInstanceOf(AmaHelloInventoryTestMessageData.class);
        assertThat( ((AmaHelloInventoryTestMessageData)pollRes.getBody()[0].getData()).getDesc()).isEqualTo("Hello A3!");
    }
	
	@Test
    public void a3CloudSynPushMessage() {
        //Onboarding
        noBoardSuccess();
        
        AmacITUtil.executeDelay( () -> {
            var pollRes = upStreamRests.keepAlive();
            var messageId = pollRes.getBody()[0].getHeader().getMessageId();
            System.out.println("Message Id : " + messageId);
            var respBody = new AmaResponseMessageData(messageId, true, 0, "Hello A3!", null, null);
            var header = new AmaMessageHeader(systemId, null, null);
            upStreamRests.reportMessage( Collections.singletonList( new AmaMessage(header, respBody) ));
        }, 1000);
        
        //Sender Hello Inventory Message
        var header = new AmaMessageHeader(systemId, null, null);
        var helloInventory = new AmaHelloInventoryTestMessageData("Hello A3 Cloud!");
        var result = downStreamRests.pushSynMessage( new AmaMessage(header, helloInventory) );
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getData()).isInstanceOf(AmaResponseMessageData.class);
        assertThat( ((AmaResponseMessageData)result.getBody().getData()).isSuccessful() ).isTrue();
        assertThat( ((AmaResponseMessageData)result.getBody().getData()).getErrorMessage() ).isEqualTo("Hello A3!");
    }
	
	
	@After
    public void destroy() {
	    downStreamRests.pushMessages( Collections.singletonList(AmaMessageFactory.createClearMessage(systemId)) );
    }
}
