/**
 * 
 */
package com.aerohive.a3.amacit.amac;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.aerohive.a3.amacit.util.AmaMessageFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author zjie
 *
 */
@RunWith(JUnit4.class)
public class TestData {
    
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void makeDataForVoilation() throws JsonProcessingException {
        var toDate = Instant.now();
        var fromDate = toDate.minus(Duration.ofDays(300));
        var cout = 2000;
        
        var resList = new ArrayList<Long>();
        for (var i=0; i<cout; i++) {
            resList.add(RandomUtils.nextLong(fromDate.toEpochMilli(), toDate.toEpochMilli()));
        }
        
        var rest = mapper.writeValueAsString(AmaMessageFactory.createViolationMessage("12345678", resList));
        System.out.println(rest);
    }
}
