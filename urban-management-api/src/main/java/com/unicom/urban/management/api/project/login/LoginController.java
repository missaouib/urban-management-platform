package com.unicom.urban.management.api.project.login;

import com.unicom.urban.management.api.framework.security.JwtTokenManager;
import com.unicom.urban.management.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录Controller
 *
 * @author liukai
 */
@RestController
public class LoginController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenManager jwtTokenManager;


    @PostMapping("/login")
    public Result login(@RequestParam String username, @RequestParam String password, HttpServletResponse response, HttpServletRequest request) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            String token = jwtTokenManager.createToken(authentication);
            return Result.success(token);
        } catch (AuthenticationException e) {
            return Result.fail(11, "账号或密码错误");
        }

    }


}
