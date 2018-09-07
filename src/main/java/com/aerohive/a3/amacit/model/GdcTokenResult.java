/**
 * 
 */
package com.aerohive.a3.amacit.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author zjie
 *
 */
@Data
public class GdcTokenResult {

    @JsonProperty("access_token")
    String accessToken;
}
