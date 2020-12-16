package com.unicom.urban.management.service.eventfile;

import com.unicom.urban.management.dao.eventfile.EventFileRepository;
import com.unicom.urban.management.pojo.entity.EventFile;
import com.unicom.urban.management.service.kv.KVService;
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
    @Autowired
    private KVService kvService;

    public EventFile save(EventFile eventFile) {
        return eventFileRepository.save(eventFile);
    }

    public List<EventFile> saveAll(List<EventFile> eventFileList) {
        return eventFileRepository.saveAll(eventFileList);
    }

    public List<EventFile> joinEventFileListToObjet(List<String> fileUrlList,Integer fileType) {
        List<EventFile> eventFileList = new ArrayList<>(fileUrlList.size());
        if (CollectionUtils.isNotEmpty(fileUrlList)) {
            for (String s : fileUrlList) {
                EventFile eventFile = new EventFile();
                eventFile.setFilePath(s);
                eventFile.setFileName(s.contains("/") ? s.split("/")[s.split("/").length - 1] : "");
                eventFile.setManagement(kvService.findByTableNameAndFieldNameAndValue("eventFile", "management", "处置前").get(0));
                switch (fileType){
                    case 1:
                        eventFile.setFileType(kvService.findByTableNameAndFieldNameAndValue("eventFile", "fileType", "图片").get(0));
                        break;
                    case 2:
                        eventFile.setFileType(kvService.findByTableNameAndFieldNameAndValue("eventFile", "fileType", "视频").get(0));
                        break;
                    case 3:
                        eventFile.setFileType(kvService.findByTableNameAndFieldNameAndValue("eventFile", "fileType", "音频").get(0));
                        break;
                    default:
                        break;
                }
                eventFileList.add(eventFile);
            }
        }
        return this.saveAll(eventFileList);
    }
    public List<EventFile> joinEventFileListToObjetAfter(List<String> fileUrlListUrlListAfter,Integer fileType) {
        List<EventFile> eventFileList = new ArrayList<>(fileUrlListUrlListAfter.size());
        if (CollectionUtils.isNotEmpty(fileUrlListUrlListAfter)) {
            for (String s : fileUrlListUrlListAfter) {
                EventFile eventFile = new EventFile();
                eventFile.setFilePath(s);
                eventFile.setFileName(s.contains("/") ? s.split("/")[s.split("/").length - 1] : "");
                eventFile.setManagement(kvService.findByTableNameAndFieldNameAndValue("eventFile", "management", "处置后").get(0));
                switch (fileType){
                    case 1:
                        eventFile.setFileType(kvService.findByTableNameAndFieldNameAndValue("eventFile", "fileType", "图片").get(0));
                        break;
                    case 2:
                        eventFile.setFileType(kvService.findByTableNameAndFieldNameAndValue("eventFile", "fileType", "视频").get(0));
                        break;
                    case 3:
                        eventFile.setFileType(kvService.findByTableNameAndFieldNameAndValue("eventFile", "fileType", "音频").get(0));
                        break;
                    default:
                }
                eventFileList.add(eventFile);
            }
        }
        return this.saveAll(eventFileList);
    }
}
