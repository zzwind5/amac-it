package com.aerohive.a3.amacit.util;

import com.aerohive.a3.ama.message.AmaMessage;
import com.aerohive.a3.ama.message.AmaMessageHeader;
import com.aerohive.a3.ama.message.common.AmaResponseMessageData;
import com.aerohive.a3.ama.message.common.AmaResponseMessageData.AmaResponseType;
import com.aerohive.a3.ama.message.inventory.A3ConnectMessageData;
import com.aerohive.a3.ama.message.inventory.A3DisassociateMessageData;

public class AmaMessageFactory {

    public static AmaMessage createResponseMessage(final AmaMessage requestMsg, final boolean successful) {
        var header = new AmaMessageHeader(requestMsg.getHeader().getSystemId(), null, null);
        
        var responseType = requestMsg.getData() instanceof A3ConnectMessageData ? 
                AmaResponseType.ONBOARDING : AmaResponseType.DO_NOT_CARE;
        var errorCode = successful ? 0 : -1;
        var body = new AmaResponseMessageData(requestMsg.getHeader().getMessageId(), 
                successful, errorCode, null, responseType, null);
        
        return new AmaMessage(header, body);
    }
    
    public static AmaMessage createOnBoardMessage(final String systemId) {
        var header = new AmaMessageHeader(systemId, null, null);
        var body = new A3ConnectMessageData(null, null, null, null, null, null, null, null, null, null, null, null);
        
        return new AmaMessage(header, body);
    }
    
    public static AmaMessage createClearMessage(final String systemId) {
        var header = new AmaMessageHeader(systemId, null, null);
        var body = new A3DisassociateMessageData();
        
        return new AmaMessage(header, body);
    }
}
