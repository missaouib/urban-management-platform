package com.unicom.urban.management.api.framework.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class TokenController extends TokenEndpoint {

    @PostMapping("/asd")
    public void token(Principal principal, @RequestParam Map<String, String> parameters) {
        try {
            ResponseEntity<OAuth2AccessToken> oAuth2AccessTokenResponseEntity = postAccessToken(principal, parameters);


        } catch (HttpRequestMethodNotSupportedException e) {
            e.printStackTrace();
        }
    }

}
