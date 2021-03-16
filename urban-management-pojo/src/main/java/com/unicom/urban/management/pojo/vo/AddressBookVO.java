package com.unicom.urban.management.pojo.vo;

import lombok.Data;

/**
 * 通讯录VO
 *
 * @author jiangwen
 */
@Data
public class AddressBookVO {

    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号码
     */
    private String phone;

}
