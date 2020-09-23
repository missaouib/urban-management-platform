# 用户组织机构starter[![GitLab](http://192.168.30.124/assets/favicon-7901bd695fb93edb07975966062049829afb56cf11511236e61bcf425070e36e.png)](http://192.168.30.124/wangzj129-grouppath/develop/firebirds-eye-starter.git)

## 介绍

> 用户组织机构starter是一款便捷的通用用户组织机构权限管理组件。使用者可以通过对该组件的引用，来获取对于用户、组织机构、权限、多租户、功能管理、数据权限等多种通用系统的管理能力。组件运用一种简单、快捷的机制，对使用者提供了数据库字段扩展功能、返回数据的对照映射功能等。是一款拥有高度灵活性的组件，能极大程度的提升使用者的研发效率
>
> ####  **特性**
> - 提供标准的用户、组织机构、权限、多租户、功能管理、数据权限等基础功能的数据操作实现；
> - 提供了Controller层与Service层两种集成方式；
> - 支持数据字段扩展功能，用户组织机构权限组件提供了数据字段扩展功能，不需要对原有方法进行重写，即可对原有提供的数据字段进行扩展；
> - 组件支持控制层扩展、服务层扩展和数据持久层扩展，支持对原有方法进行重写。同时使用者可以依据需求进行方法新增；


## 使用

###  一、组件
- 引入依赖开始使用组件

```xml
<dependency>
    <groupId>cn.unicom.hlj.snr.firebird</groupId>
    <artifactId>firebirds-eye-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```


###  二、集成方式
> **用户组织机构starter组件提供了Controller层与Service层两种集成方式**

#### （1）Service类调用(具体参数请参考附件:用户组织机构使用文档)

-  ##### DeptService  部门管理服务
	1. list --查询部门列表，只查询本级
	2. treeList --查询部门树列表
	3. profileTreeList --查询部门树简要信息列表
	4. info --查询部门详情(可查询父级、子级)
	5. info --查询部门详情(只查询当前层级)
	6. insert --新增部门
	7. update --修改部门
	8. updateMap --修改部门（如果Map中有key则更新，没有则忽略，如果key的值为null，则更新为null）
	9. delete --物理删除部门树，将删除部门的一切关联表
	10. remove --逻辑删除部门，数据仍然保留
	11. remove --批量逻辑删除部门，数据仍然保留
-  ##### PermissionGroupService 功能组管理服务
	1. list --查询功能组列表
	2. profileList --查询功能组摘要列表
	3. info --查看功能组详情
	4. detail --查看功能组详情，返回功能组基本信息，功能点列表，租户列表
	5. insert --创建功能组
	6. update --修改功能组
	7. updateMap --修改功能组（如果Map中有key则更新，没有则忽略，如果key的值为null，则更新为null）
	8. delete --物理删除功能组
	9. remove --逻辑删除功能组（数据仍保留）
	10. remove --批量逻辑删除功能组（数据仍保留）
-  ##### PermissionService 功能点管理服务
	1. list --查询功能点列表，只查询本级
	2. treeList --查询功能点树列表
	3. profileTreeList --查询功能点树简要信息列表
	4. info --查询功能点详情(可查询父级、子级)
	5. insert --新增功能点
	6. update --修改功能点
	7. updateMap --修改功能点（如果Map中有key则更新，没有则忽略，如果key的值为null，则更新为null）
	8. delete --物理删除功能树，将删除功能点的一切关联表
	9. remove --逻辑删除功能点，数据仍然保留
	10. remove --批量逻辑删除功能点，数据仍然保留
-  ##### RoleService 角色管理服务
	1. list --查询角色列表
	2. profileList --查询角色摘要列表
	3. info --查询角色详情
	4. insert --新增角色
	5. update --修改角色
	6. updateMap --修改角色（如果Map中有key则更新，没有则忽略，如果key的值为null，则更新为null）
	7. delete --物理删除角色，角色的关联表也将被永久删除
	8. remove --逻辑删除角色，数据仍然保留
	9. remove --批量逻辑删除角色，数据仍然保留
-  ##### TenantService 租户管理服务
	1. list --查询租户列表
	2. profileList --查询租户摘要列表
	3. info --获取租户详情
	4. insert --新增租户
	5. update --修改租户
	6. updateMap --修改租户（如果Map中有key则更新，没有则忽略，如果key的值为null，则更新为null）
	7. delete --物理删除租户信息
	8. remove --逻辑删除租户信息
	9. remove --批量逻辑删除租户信息
-  ##### UserService 用户管理服务
	1. list --查询用户列表
	2. profileList --查询用户摘要列表
	3. info --查看用户详情
	4. detail --根据 userId 查看用户详情，返回用户基本信息，用户角色列表，用户所属组织列表，用户具有的特殊的组织角色权限列表
	5. insert --创建用户
	6. update --修改用户
	7. updateMap --修改用户（如果Map中有key则更新，没有则忽略，如果key的值为null，则更新为null）
	8. delete --物理删除用户信息
	9. remove --逻辑删除用户信息
	10. remove --批量逻辑删除用户信息
	11. changePassword --修改密码
	12. resetPassword --重置密码
- ##### PermissionGroupPermissionRelationService  功能组-功能点关系管理服务
	1. insert --创建功能组-功能点关系
	2. delete -- 物理删除功能组-功能点关系
	3. listPermissionGroupByPermission --获取指定功能点的功能组列表
	4. listPermissionByPermissionGroup --获取指定功能组的功能点列表
	5. arrangePermissionGroupByPermission --调整指定功能点的功能组列表
	6. arrangePermissionByPermissionGroup --调整指定功能组的功能点列表
- ##### RolePermissionRelationService 角色-功能点关系管理服务
	1. insert --创建角色-功能点关系
	2. delete --删除角色-功能点关系
	3. listRoleByPermission --获取指定功能点的角色列表(分页)
	4. listPermissionByRole --获取指定角色的功能点列表(分页)
	5. listPermissionByRole --获取指定角色的功能点列表(不分页)
	6. listRoleByPermission --获取指定功能点的角色列表(不分页)
	7. arrangePermissionByRole --调整指定角色的功能点列表
	8. arrangeRoleByPermission --调整指定功能点的角色列表
- ##### TenantPermissionGroupRelationService 租户-功能组关系管理服务
	1. insert --创建租户-功能组关系
	2. delete --物理删除租户-功能组关系
	3. listTenantByPermissionGroup --获取指定功能组的租户列表
	4. listPermissionGroupByTenant --获取指定租户的功能组列表
	5. arrangeTenantByPermissionGroup --调整指定功能组的租户列表
	6. arrangePermissionGroupByTenant --调整指定租户的功能组列表
- ##### UserDeptRelationService 用户-部门关系管理服务
	1. insert --创建用户-部门关系
	2. setPositionType --设置人员在部门的岗位
	3. delete --删除用户-部门关系
	4. listDeptByUser --获取指定用户的部门列表
	5. listUserByDept --获取指定部门的用户列表
	6. arrangeUserByDept --调整指定部门的用户列表
	7. arrangeDeptByUser --调整指定用户的部门列表
- ##### UserRoleDeptRelationService 用户-角色-部门 关系管理服务
	1. insert --创建用户-角色-部门关系
	2. delete --删除用户-角色-部门关系
	3. listUserDeptByRole --获取具有指定角色的用户-部门列表
	4. listRoleDeptByUser --获取指定用户的角色-部门列表(分页)
	5. listRoleDeptByUser --获取指定用户的角色-部门列表(不分页)
	6. listRoleUserDeptByPermission --获取指定功能点的角色-用户-部门列表
	7. listRoleUserDeptByUserAndRole --获取指定的角色指定用户的 用户-角色-部门列表
	8. listUserRoleDeptByUserAndRoleList --获取指定的角色集合和指定用户的 用户-角色-部门列表(分页)
	9. listUserRoleDeptByUserAndRoleList --获取指定的角色集合和指定用户的 用户-角色-部门列表(不分页)
	10. arrangeUserDeptByRole --调整指定角色的用户-部门列表
	11. arrangeRoleDeptByUser --调整指定用户的角色-部门列表
- ##### UserRoleRelationService 用户-角色关系管理服务
	1. insert --创建用户-角色关系
	2. delete --删除用户-角色关系
	3. listUserByRole --获取具有指定角色的用户列表
	4. listRoleByUser --获取指定用户的角色列表
	5. arrangeUserByRole --调整指定角色的用户列表
	6. arrangeRoleByUser --调整指定用户的角色列表

#### （2）Controller接口调用

- ##### DeptController 部门管理
	1. 查询部门列表，只查本级：dept/list
	2. 查询部门树列表：dept/tree-list
	3. 查询部门树简要信息列表，用于树形下拉框展示，以及其他被关联的地方：dept/profile-tree-list
	4. 查询部门详情：dept/detail
	5. 查询部门信息：dept/info
	6. 新增部门：dept/insert
	7. 修改部门：dept/update
	8. 修改部门（Map中有key则更新，没有则忽略，如果key的值为null，则更新为null）：dept/update-map
	8. 物理删除部门树，将删除部门的一切关联表：dept/delete
	9. 逻辑删除部门，数据仍然保留：dept/remove
	10. 批量逻辑删除部门，数据仍然保留：dept/batch-remove
- ##### PermissionController 功能点管理
	1. 根据条件查询权限列表：permission/list
	2. 根据条件查询功能点树：permission/tree-list
	3. 根据条件查询功能点树详情：permission/profile-tree-list
	4. 查看功能点详情：permission/info
	5. 创建权限：permission/insert
	6. 修改部门：permission/update
	7. 修改部门（Map中有key则更新，没有则忽略，如果key的值为null，则更新为null）：permission/update-map
	8. 物理删除功能点信息：permission/delete
	9. 逻辑删除功能点信息：permission/remove
	10. 批量逻辑删除功能点信息：permission/batch-remove
- ##### TenantController 租户管理
	1. 查询租户列表：tenant/list
	2. 查询租户摘要列表：tenant/profile-list
	3. 查看租户详情：tenant/info
	4. 创建租户：tenant/insert
	5. 修改租户：tenant/update
	7. 修改租户信息：tenant/update-map
	8. 物理删除租户：tenant/delete
	9. 逻辑删除租户：tenantr/emove
	10. 批量逻辑删除租户：tenant/batch-remove
- ##### UserController 用户管理
	1. 查询用户信息列表：user/list
	2. 查询用户摘要列表：user/profile-list
	3. 查询用户信息：user/info
	4. 根据 userId 查看用户详情：user/detail
	5. 插入用户信息：user/insert
	6. 修改用户：user/update
	7. 根据 userId 修改用户信息：user/update-map
	8. 根据 userId 物理删除用户：user/delete
	9. 根据 userId 逻辑删除用户：user/remove
	10. 批量逻辑删除用户：user/batch-remove
	11. 修改密码：user/change-password
	12. 重置密码：user/reset-password
- ##### RoleController 角色管理
	1. 查询角色列表：role/list
	2. 查询角色摘要列表：role/profile-list
	3. 查看角色详情：role/info
	4. 创建角色：role/insert
	5. 修改角色：role/update
	6. 修改角色：role/update-map
	7. 物理删除角色：role/delete
	8. 逻辑删除角色：role/remove
	9. 批量逻辑删除角色：role/batch-remove
- ##### PermissionGroupController 功能组管理
	1. 查询功能组列表：permission-group/list
	2. 查询功能组摘要列表：permission-group/profile-list
	3. 查看功能组详情：permission-group/info
	4. 查看功能组详情，返回功能组基本信息，功能点列表，租户列表：permission-group/detail
	5. 创建功能组：permission-group/insert
	6. 修改功能组：permission-group/update
	7. 修改功能组，注意Map中有key则更新，没有则忽略，如果key的值为null，则更新为null：permission-group/update-map
	8. 物理删除功能组：permission-group/delete
	9. 逻辑删除功能组：permission-group/remove
	10. 批量逻辑删除功能组：permission-group/batch-remove
- ##### PermissionGroupPermissionRelationController 功能组-功能点关系管理
	1. 创建功能组-功能点关系：permission-group-permission/insert
	2. 物理删除功能组-功能点关系：permission-group-permission/delete
	3. 获取指定功能点的功能组列表：permission-group-permissionl/ist-permission-group-by-permission
	4. 获取指定功能组的功能点列表：permission-group-permission/list-permission-by-permission-group
	5. 调整指定功能点的功能组列表：permission-group-permission/arrange-permission-group-by-permission
	6. 调整指定功能组的功能点列表：permission-group-permission/arrange-permission-by-permission-group
- ##### RolePermissionRelationController 角色-功能点关系管理
	1. 创建角色-功能点关系：role-permission/insert
	2. 物理删除角色-功能点关系：role-permission/delete
	3. 获取指定功能点的角色列表：role-permission/list-role-by-permission
	4. 获取指定角色的功能点列表：role-permission/list-permission-by-role
	5. 调整指定功能点的角色列表：role-permission/arrange-role-by-permission
	6. 调整指定角色的功能点列表：role-permission/arrange-permission-by-role
- ##### TenantPermissionGroupRelationController 租户-功能组关系管理
	1. 创建租户-功能组关系：tenant-permission-group/insert
	2. 物理删除租户-功能组关系：tenant-permission-group/delete
	3. 获取指定功能组的租户列表：tenant-permission-group/list-tenant-by-permission-group
	4. 获取指定租户的功能组列表：tenant-permission-group/list-permission-group-by-tenant
	5. 调整指定功能组的租户列表：tenant-permission-group/arrange-tenant-by-permission-group
	6. 调整指定租户的功能组列表：tenant-permission-group/arrange-permission-group-by-tenant
- ##### UserDeptRelationController 用户-部门关系管理
	1. 创建用户-部门关系：user-dept/insert
	2. 设置人员在部门的岗位：user-dept/set-position-type
	3. 删除用户-部门关系：user-dept/delete
	4. 获取指定用户的部门列表：user-dept/list-dept-by-user
	5. 获取指定部门的用户列表：user-dept/list-user-by-dept
	6. 调整指定部门的用户列表：user-dept/arrange-user-by-dept
	7. 调整指定用户的部门列表：user-dept/arrange-dept-by-user
- ##### UserRoleDeptRelationController 用户-角色-部门 关系管理
	1. 创建用户-角色-部门关系：user-role-dept/insert
	2. 删除用户-角色-部门关系：user-role-dept/delete
	3. 根据userId获取指定用户的角色-用户-部门关系列表：user-role-dept/list-role-dept-by-user
	4. 根据userId或roleId获取指定用户的角色-用户-部门关系列表：user-role-dept/list-role-dept-by-user-and-role
	5. 获取指定功能点的角色-用户-部门关系列表：user-role-dept/list-role-dept-by-permission
	6. 获取拥有指定角色的角色-用户-部门关系列表：user-role-dept/list-user-dept-by-role
	7. 调整指定用户的角色-用户-部门关系：user-role-dept/arrange-role-dept-by-user
	8. 调整指定角色的角色-用户-部门关系：user-role-dept/arrange-user-by-role
- ##### UserRoleRelationController 用户-角色关系管理
	1. 创建用户-角色关系：user-role/insert
	2. 删除用户-角色关系：user-role/delete
	3. 获取指定用户的角色列表：user-role/list-role-by-user
	4. 获取拥有指定角色的用户列表：user-role/list-user-by-role
	5. 调整指定用户的角色列表：user-role/arrange-role-by-user
	6. 调整指定角色的用户列表：user-role/arrange-user-by-role


### 三、 数据字段扩展
> ##### 用户组织机构starter组件提供了数据字段扩展功能，不需要对原有方法进行重写，即可对原有提供的数据字段进行扩展。
> 
>  **DO扩展**、 **DTO扩展** 、 **Criteria扩展** 

####  （1）DO扩展

 - #####  DO 扩展示例

```java
public final class YourDeptDO extends DeptDO<SnrFeTenantDO> {

	private String deptNameAbb;
	
	public String getDeptNameAbb() { return deptNameAbb; }
	
	public void setDeptNameAbb (String deptNameAbb) { this.deptNameAbb= deptNameAbb; }
	
} 

```

#### （2） DTO扩展

 - #####  DTO 扩展示例

```java
public final class YourDeptDTO extends DeptDTO<SnrFeDeptDTO, SnrFeTenantProfileDTO> {

	private String deptNameAbb;
	
	public String getDeptNameAbb() { return deptNameAbb; }
	
	public void setDeptNameAbb (String deptNameAbb) { this.deptNameAbb= deptNameAbb; }

}
```

#### （3）Criteria扩展

 - #####  Criteria 扩展示例 

```java
public final class YourDeptCriteria extends DeptCriteria {

	private String deptNameAbb;
	
	public String getDeptNameAbb() { return deptNameAbb; }
	
	public void setDeptNameAbb (String deptNameAbb) { this.deptNameAbb= deptNameAbb; }

}
```
### 四、 控制层扩展

#### Controller扩展

> 用户组织机构starter组件为使用者提供了控制层的扩展功能。通过对原有提供的Controller类的继承，可以对原有提供的方法进行重写，或者新增方法。在对Controller类进行扩展的过程中，需要通过**@ApiVersion("版本号")**注解进行版本设置。使用方法如下示例。组件提供的原有方法版本号默认为“.0.1”。如不对方法进行版本设置，则默认版本号为“0”。

 - ##### Controller扩展示例 

```java
@RestController("deptController")
@RequestMapping(path = "dept")
public class YourDeptController extends DeptController {

    private DeptService deptService;

    @Autowired
    public void setDeptService(DeptService deptService) {
        this.deptService = deptService;
    }

	//list方法 版本.0.1（原有方法）
	@ApiVersion(".0.1")
    @RequestMapping(value = "list",
            method = RequestMethod.POST,
            produces = "application/json;charset=utf-8")
    public PageResultDTO<SnrFeDeptDTO> list(@RequestBody SnrFeDeptCriteria deptCriteria) {
        this.initCriteria(deptCriteria);
        PageInfo<SnrFeDeptDTO> pageInfo = deptService.list(deptCriteria, SnrFeDeptDTO.class);
        PageResultDTO<SnrFeDeptDTO> pageResultDTO = new PageResultDTO<>(pageInfo);
        return pageResultDTO;
    }

    //list方法 版本1.0（扩展方法）
    @ApiVersion("1.0")
    @RequestMapping(value = "list",
            method = RequestMethod.POST,
            produces = "application/json;charset=utf-8")
    public PageResultDTO<SampleDeptDTO> list1(@RequestBody SnrFeDeptCriteria deptCriteria) {
        PageResultDTO<SampleDeptDTO> PageResultDTO = new PageResultDTO<>();
        return PageResultDTO;
    }
```
### 五、 服务层扩展

#### Service扩展

> 用户组织机构starter组件提供了服务层的扩展功能。通过对原有提供的Service类的继承，可以对原有提供的方法进行重写，或者新增方法。使用方法如下示例。

 - ##### Service扩展示例 

```java
// Service 扩展示例
public class YourDeptServiceImpl extends AbstractDeptServiceImpl<
        SnrFeDeptDO,
        SnrFeUserRoleDeptRelationDO,
        SnrFeUserDeptRelationDO> {
	
	//（情况一） 对方法进行重写
    @Override
    public <DTO extends DeptDTO, Criteria extends DeptCriteria> PageInfo<DTO> list(Criteria criteria, Class<DTO> dtoClass) {
        PageInfo<DTO> pageInfo = new PageInfo<>();
        return pageInfo;
    }
    
    //（情况二）在对已有方法中的入参数据进行前置处理
    @Override
    public <DTO extends DeptDTO, Criteria extends DeptCriteria> PageInfo<DTO> list(Criteria criteria, Class<DTO> dtoClass) {
        return super.list(criteria, dtoClass);
    }
	
	//（情况三）新增方法
	public <DTO extends DeptDTO, Criteria extends DeptCriteria> PageInfo<DTO> singelList(Criteria criteria, Class<DTO> dtoClass) {
        PageInfo<DTO> pageInfo = new PageInfo<>();
        return pageInfo;
    }
    
}

```
### 六、 数据持久层扩展

> 用户组织机构starter组件提供了数据持久层的扩展功能。通过对原有提供的Mapper类的继承，可以对原有提供的方法进行重写，或者新增方法。使用方法如下示例。


#### Mapper扩展

 - #####  Mapper 扩展示例-java

```java
public interface YourDeptMapper extends AbstractDeptMapper<SnrFeDeptDO> {

    <Criteria extends DeptCriteria>
    List<SnrFeDeptDO> selectAllForId (Criteria criteria);
    
}
```

 - #####  Mapper 扩展示例-xml

```xml

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="%{mapper}">
    <resultMap id="BaseResultMap" type="%{DO}" extends="%{superResult}">
    
    </resultMap>

    <snr:generate table="snr_fe_dept"></snr:generate>

    <select id="selectAllForId" resultMap="BaseResultMap" flushCache="true" parameterType="cn.unicom.hlj.snr.firebird.eye.model.criteria.DeptCriteria">
        SELECT snr_fe_dept.dept_id
        FROM snr_fe_dept as snr_fe_dept
        <where>
            <if test="deptId != null and deptId!=''" >
                AND snr_fe_dept.dept_id = #{deptId,jdbcType=VARCHAR}
            </if>
        </where>
    </select>
</mapper>
```

### 七、原有数据库迁移办法

>  如果需要保留原有数据库中数据,则可进行数据库迁移
>  在迁移过程中,  **snr_fe_permission **  和  **snr_fe_dept** 表中的数据需要按如下数据库数据迁移sql脚本示例进行数据迁移  

- #####  数据库数据迁移sql脚本示例
- 1. 复制原表数据到新表中
```sql
INSERT INTO snr_fe_permission
 (
	snr_fe_permission.permission_id,
	snr_fe_permission.permission_code,
	snr_fe_permission.permission_name,
	snr_fe_permission.permission_pid,
	snr_fe_permission.update_time,
	snr_fe_permission.create_time,
	snr_fe_permission.delete_token,
	snr_fe_permission.type,
	snr_fe_permission.available,
	snr_fe_permission.mold,
	snr_fe_permission.displayorder,
	snr_fe_permission.app_key
)
SELECT 
原数据表.permission_id as permission_id,
原数据表.permission_code as permission_code,
原数据表.permission_name as permission_name,
原数据表.permission_pid as permission_pid,
原数据表.update_time as update_time,
原数据表.create_time as create_time,
原数据表.delete_token as delete_token,
原数据表.type as type,
原数据表.available as available,
原数据表.mold as mold,
原数据表.displayorder as displayorder,
原数据表.app_key as app_key 
FROM 原数据表
```
- 2.修改第一层级数据的path,permission_level

```sql
UPDATE snr_fe_permission 
SET snr_fe_permission.path='/',snr_fe_permission.permission_level=1
WHERE snr_fe_permission.permission_pid=0
```

- 3.依次修改除第一层级外节点的path,permission_level

```sql

UPDATE snr_fe_permission AS t2
SET 
	t2.path =
	(SELECT CONCAT(t4.path, t4.nid, '/') 
	 FROM snr_fe_permission as t4 
	 WHERE t4.permission_id=t2.permission_pid),
	permission_level=4 (可变位置1)
WHERE t2.permission_pid 
IN (SELECT t3.permission_id FROM snr_fe_permission as t3 WHERE t3.permission_level=3(可变位置2) )

	例：要修改第二层权限的path  
	那么位置1的值就是2   位置2的值就是1
	举例子：要修改第三层权限的path  
	那么位置1的值就是3   位置2的值就是2
	。。。以此类推

```

## 附件:用户组织机构使用文档

附件下载地址： [详见知识管理平台](http://10.64.43.244:12301/componentDetail?id=270)

附件使用方法：附件解压后，请点击 index.html 文件开始阅览

