package com.unicom.urban.management.service.eventfile;

import com.unicom.urban.management.dao.eventfile.EventFileRepository;
import com.unicom.urban.management.pojo.entity.EventFile;
import com.unicom.urban.management.service.kv.KVService;
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

    public List<EventFile> joinEventFileListToObjet(List<String> imageUrlList) {
        /* todo 此处应该是图片集合 音频集合 视频集合的总和 目前只有图片 */
        List<EventFile> eventFileList = new ArrayList<>(imageUrlList.size());
        if (imageUrlList.size() > 0) {
            for (String s : imageUrlList) {
                EventFile eventFile = new EventFile();
                eventFile.setFilePath(s);
                eventFile.setFileName(s.contains("/") ? s.split("/")[s.split("/").length - 1] : "");
                eventFile.setManagement(kvService.findByTableNameAndFieldNameAndValue("eventFile", "management", "处置前").get(0));
                eventFile.setFileType(kvService.findByTableNameAndFieldNameAndValue("eventFile", "fileType", "图片").get(0));
                eventFileList.add(eventFile);
            }
        }
        /* todo 音频 */
        /* todo 视频 */
        return this.saveAll(eventFileList);
    }
    public List<EventFile> joinEventFileListToObjetAfter(List<String> imageUrlListAfter) {
        /* todo 此处应该是图片集合 音频集合 视频集合的总和 目前只有图片 */
        List<EventFile> eventFileList = new ArrayList<>(imageUrlListAfter.size());
        if (imageUrlListAfter.size() > 0) {
            for (String s : imageUrlListAfter) {
                EventFile eventFile = new EventFile();
                eventFile.setFilePath(s);
                eventFile.setFileName(s.contains("/") ? s.split("/")[s.split("/").length - 1] : "");
                eventFile.setManagement(kvService.findByTableNameAndFieldNameAndValue("eventFile", "management", "处置后").get(0));
                eventFile.setFileType(kvService.findByTableNameAndFieldNameAndValue("eventFile", "fileType", "图片").get(0));
                eventFileList.add(eventFile);
            }
        }
        /* todo 音频 */
        /* todo 视频 */
        return this.saveAll(eventFileList);
    }
}
