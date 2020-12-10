package com.unicom.urban.management.pojo.dto;

import com.unicom.urban.management.pojo.annotations.validation.MobileNumber;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Data
public class UserIdListDTO {
    private String userId;
    private String roleId;
    private Integer checkbox;
    private List<Map<String,Object>> userIdList;
}
