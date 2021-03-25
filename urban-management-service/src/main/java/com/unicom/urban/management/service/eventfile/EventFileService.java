package com.unicom.urban.management.service.eventfile;

import com.unicom.urban.management.dao.eventfile.EventFileRepository;
import com.unicom.urban.management.pojo.entity.EventFile;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 网格
 *
 * @author jiangwen
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class EventFileService {

    @Autowired
    private EventFileRepository eventFileRepository;

    public EventFile save(EventFile eventFile) {
        return eventFileRepository.save(eventFile);
    }

    public List<EventFile> saveAll(List<EventFile> eventFileList) {
        return eventFileRepository.saveAll(eventFileList);
    }

    public List<EventFile> joinEventFileListToObjet(List<String> fileUrlList, EventFile.FileType fileType) {
        List<EventFile> eventFileList = new ArrayList<>(fileUrlList.size());
        if (CollectionUtils.isNotEmpty(fileUrlList)) {
            for (String s : fileUrlList) {
                EventFile eventFile = new EventFile();
                eventFile.setFilePath(s);
                eventFile.setFileName(s.contains("/") ? s.split("/")[s.split("/").length - 1] : "");
                eventFile.setManagement(1);
                eventFile.setFileType(fileType);
                eventFileList.add(eventFile);
            }
        }
        return this.saveAll(eventFileList);
    }

    public List<EventFile> joinEventFileListToObjetAfter(List<String> fileUrlListUrlListAfter, EventFile.FileType fileType) {
        List<EventFile> eventFileList = new ArrayList<>(fileUrlListUrlListAfter.size());
        if (CollectionUtils.isNotEmpty(fileUrlListUrlListAfter)) {
            for (String s : fileUrlListUrlListAfter) {
                EventFile eventFile = new EventFile();
                eventFile.setFilePath(s);
                eventFile.setFileName(s.contains("/") ? s.split("/")[s.split("/").length - 1] : "");
                eventFile.setManagement(2);
                eventFile.setFileType(fileType);
                eventFileList.add(eventFile);
            }
        }
        return this.saveAll(eventFileList);
    }
}
