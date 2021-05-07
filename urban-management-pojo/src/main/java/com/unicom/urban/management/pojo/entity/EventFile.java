package com.unicom.urban.management.pojo.entity;

import com.unicom.urban.management.common.exception.BusinessException;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 事件附件实体类
 *
 * @author jiangwen
 */
@Entity
public class EventFile {

    private String id;

    private String fileName;

    private FileType fileType;

    private String filePath;

    private Integer management;


    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getManagement() {
        return management;
    }

    public void setManagement(Integer management) {
        this.management = management;
    }

    @Convert(converter = EventFile.FileTypeConverter.class)
    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    /**
     * 一定要实现BaseEnum
     *
     * @author liukai
     */
    public enum FileType implements BaseEnum {


        IMAGE(1, "图片"),
        VIDEO(2, "视频"),
        AUDIO(3, "音频");

        FileType(Integer value, String description) {
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

    public static class FileTypeConverter implements AttributeConverter<EventFile.FileType, Integer> {

        @Override
        public Integer convertToDatabaseColumn(EventFile.FileType attribute) {
            if (attribute == null) {
                throw new BusinessException("Unknown fileType text  ");
            }
            return attribute.getValue();

        }

        @Override
        public EventFile.FileType convertToEntityAttribute(Integer dbData) {
            for (EventFile.FileType fileType : EventFile.FileType.values()) {
                if (fileType.getValue().equals(dbData)) {
                    return fileType;
                }
            }
            throw new BusinessException("Unknown fileType text : " + dbData);
        }
    }


}
