/**
 * 
 */
package com.aerohive.a3.amacit.model;

import lombok.Data;

/**
 * @author zjie
 *
 */
@Data
public class VhmBindResult {

    VhmData data;
    
    @Data
    public static class VhmData {
        String location;
    }
}
