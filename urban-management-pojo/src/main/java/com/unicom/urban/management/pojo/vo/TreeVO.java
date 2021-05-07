package com.unicom.urban.management.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TreeVO implements Serializable {

    private String id;

    private String parentId;

    private String name;

    private String levelOrNot;

    private String dtoType;


}
