package com.unicom.urban.management.dao.menu;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.Menu;

import java.util.List;

public interface MenuRepository extends CustomizeRepository<Menu, String> {
    List<Menu> findAllByMenuType_Id(String menuTypeId);

    List<Menu> findAllByParent_IdAndName(String parentId,String name);
}
