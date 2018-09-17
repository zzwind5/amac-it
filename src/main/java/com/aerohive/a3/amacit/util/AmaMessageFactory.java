package com.aerohive.a3.amacit.util;

import java.util.List;
import java.util.stream.Collectors;

import com.aerohive.a3.ama.message.AmaMessage;
import com.aerohive.a3.ama.message.AmaMessageHeader;
import com.aerohive.a3.ama.message.common.AmaResponseMessageData;
import com.aerohive.a3.ama.message.common.AmaResponseMessageData.AmaResponseType;
import com.aerohive.a3.ama.message.inventory.A3ConnectMessageData;
import com.aerohive.a3.ama.message.inventory.A3DisassociateMessageData;
import com.aerohive.a3.ama.message.report.A3RawTablesData;
import com.aerohive.a3.ama.message.report.table.A3TableViolation;
import com.google.common.collect.ImmutableList;

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
    
    public static AmaMessage createViolationMessage(final String systemId, final List<Long> times) {
        var header = new AmaMessageHeader(systemId, null, null);
        header.setOwnerId(102L);
        header.setOrgId(0L);
        
        var violationList = times.stream()
                                 .map( t -> new A3TableViolation(1001, "xxxxx", t, t+1000, "open") )
                                 .collect(Collectors.toList());
        
        var body = new A3RawTablesData(ImmutableList.copyOf(violationList));
        
        return new AmaMessage(header, body);
    }
}
