package com.unicom.urban.management.pojo.entity;

import com.unicom.urban.management.common.exception.BusinessException;
import com.unicom.urban.management.pojo.Delete;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

/**
 * 案件实体类
 *
 * @author jiangwen
 */
@Data
@Entity
@SQLDelete(sql = "update event set deleted = " + Delete.DELETE + " where id = ?")
@Where(clause = "deleted = " + Delete.NORMAL)
public class Event extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String eventCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private EventType eventType;


    /**
     * 立案条件
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private EventCondition condition;

    /**
     * 问题描述
     */
    private String represent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private DeptTimeLimit timeLimit;

    /**
     * 网格
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Grid grid;

    /**
     * 案件地址
     */
    private String location;

    /**
     * 上报人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    private Component component;

    /**
     * 问题来源
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @Deprecated
    private KV eventSource;

    /**
     * 由于KV使用问题 把eventSource替换掉
     */
    @Convert(converter = SourceConverter.class)
    private Source source = Source.SUPERVISOR;

    private Double x;

    private Double y;

    /**
     * 案件状态 比如挂账
     */
    private Integer sts;

    /**
     * 案件类型 日常管理、专项普查、其他
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private KV recType;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 案件状态 未定
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private KV eventSate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    private List<Statistics> statisticsList;

    /**
     * 诉求人
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Petitioner petitioner;

    /**
     * 事件附件实体类
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private List<EventFile> eventFileList;

    /**
     * 是否自处理
     */
    private Boolean doBySelf;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    private List<Supervise> supervises;

    /**
     * 紧急程度
     */
    @Column(columnDefinition = "tinyint")
    private Integer urgent = 0;


    /**
     * 临时字段 演示之后删除
     */
    private String componentObjId;


    /**
     * 一定要实现BaseEnum
     *
     * @author liukai
     */
    public enum Source implements BaseEnum {

        SUPERVISOR(0, "监督员上报"),
        HOT_LINE(1, "热线上报");

        Source(Integer value, String description) {
            this.value = value;
            this.description = description;
        }

        private Integer value;
        private String description;

        @Override
        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class SourceConverter implements AttributeConverter<Event.Source, Integer> {

        @Override
        public Integer convertToDatabaseColumn(Event.Source attribute) {
            if (attribute == null) {
                throw new BusinessException("Unknown source text  ");
            }
            return attribute.getValue();

        }

        @Override
        public Event.Source convertToEntityAttribute(Integer dbData) {
            for (Event.Source source : Event.Source.values()) {
                if (source.getValue().equals(dbData)) {
                    return source;
                }
            }
            throw new BusinessException("Unknown source text : " + dbData);
        }
    }

}
