package com.unicom.urban.management.dao.menu;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.Menu;

import java.util.List;

public interface MenuRepository extends CustomizeRepository<Menu, String> {
    List<Menu> findMenuByMenuType_Id(String menuTypeId);
}
