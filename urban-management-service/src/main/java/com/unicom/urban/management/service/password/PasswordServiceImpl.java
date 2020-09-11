package com.unicom.urban.management.service.password;

import com.unicom.urban.management.common.constant.SystemConstant;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {

    @Override
    public String getDefaultPassword() {
        return SystemConstant.DEFAULT_PASSWORD;
    }

}
