package com.unicom.urban.management.pojo.dto;

import com.unicom.urban.management.pojo.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 轨迹记录表
 *
 * @author liukai
 */
@Data
public class TrajectoryDTO {


    private User user;

    private Double x;

    private Double y;
}
