package com.unicom.urban.management.service.role;

import com.unicom.urban.management.pojo.entity.EventType;
import com.unicom.urban.management.pojo.entity.Menu;
import com.unicom.urban.management.pojo.vo.MenuVO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色权限配置service
 *
 * @author liubozhi
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class RolePermissionConfigurationService {

    public List<MenuVO> findMenuTree(String eventTypeId){
        List<MenuVO> menuVOList = new ArrayList<>();
        return menuVOList;
    }
}
