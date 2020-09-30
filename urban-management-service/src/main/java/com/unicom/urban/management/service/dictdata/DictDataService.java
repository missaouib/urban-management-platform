package com.unicom.urban.management.service.dictdata;

import com.unicom.urban.management.dao.dictdata.DictDataRepository;
import com.unicom.urban.management.pojo.entity.DictData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DictDataService {

    @Autowired
    private DictDataRepository dictDataRepository;

    public Page<DictData> search(Pageable pageable) {
        return dictDataRepository.findAll(pageable);
    }

    public void save(DictData dictData) {

    }

}
