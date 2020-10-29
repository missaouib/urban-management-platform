package com.unicom.urban.management.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 顾志杰
 * @date 2020/10/29-10:15
 */
@Data
public class RestReturn implements Serializable{

    private int code;
    private String message;
    private Object data;
    private Date time;
}
