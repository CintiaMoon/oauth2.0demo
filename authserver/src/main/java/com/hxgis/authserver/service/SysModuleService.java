package com.hxgis.authserver.service;


import com.hxgis.authserver.model.SysModule;
import com.hxgis.authserver.model.SysPrivilege;
import com.hxgis.authserver.model.SystemMenu;
import com.hxgis.authserver.model.TreeNode;
import com.hxgis.authserver.userRepository.RepositoryManager;
import com.hxgis.authserver.userRepository.SysModuleRepository;
import com.hxgis.authserver.userRepository.SysPrivilegeJdbc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-08-11.
 */
@Service
public class SysModuleService extends RepositoryManager<SysModule, BigDecimal> {

    @Autowired
    SysModuleRepository moduleRepository;

    @Autowired
    SysPrivilegeJdbc privilegeRepository;


    @Override
    public JpaRepository<SysModule, BigDecimal> getRepositoryDao() {
        return moduleRepository;
    }


    public List<SysModule> findBySysno(String sysno) {

        return moduleRepository.findBySysno(sysno);
    }

    public List<SysModule> findBySysnoAndParentno(String sysno, String parentNo) {
        return moduleRepository.findBySysnoAndParentno(sysno, parentNo);
    }

    public List<TreeNode> getMenuBySysno(String sysno) {

        List<SystemMenu> moduleList = privilegeRepository.getAllModuleBySysno(sysno);
        String root = sysno;
        String rootId = String.valueOf("Root");
        List<TreeNode> treeNodeList = getChildrenTreeNodes(root, rootId, moduleList, null);

        return treeNodeList;
    }

    /**
     * 把菜单数据 转换成 树形数据
     *
     * @param parentName 父节点名称
     * @param parentId   父节点id
     * @param menuList   包含的子节点列表
     * @return
     */
    public List<TreeNode> getChildrenTreeNodes(String parentName, String parentId, List<SystemMenu> menuList, String type) {
        List<TreeNode> treeNodeList = new ArrayList<>();
        int count = menuList.size();
        for (int i = 0; i < count; i++) {
            SystemMenu menu = menuList.get(i);
            String subMenuName = menu.getName();
            String subMenuId = String.valueOf(menu.getModuleno());
            String subParentId = menu.getParentno();
            boolean hasSubMenu = false;
            List<SystemMenu> subMenuList = new ArrayList<>();
            if ((i + 1) < count && menuList.get(i + 1).getParentno().equals(subMenuId)) {
                hasSubMenu = true;
                subMenuList = menuList.subList(i + 1, count);//[)左闭右开
            }

//            boolean isCheck = menu.getHasThisRight() == null ? false : true;
            boolean isCheck = false;
            if (menu.getHasThisRight() != null && menu.getHasThisRight().equals("1")) {
                isCheck = true;
            }
            String icon = "";

            if (parentId.equals(subParentId)) {
                TreeNode treeNode = new TreeNode();
                treeNode.setId(subMenuId);
                treeNode.setName(subMenuName);
                treeNode.setParentId(parentId);
                treeNode.setNodeId(String.valueOf(menu.getId()));
                if (hasSubMenu) {
                    treeNode.setChildren(getChildrenTreeNodes(subMenuName, subMenuId, menuList, type));
                }
                //默认全部展开
                treeNode.setOpen(true);
                //是否选中
                treeNode.setChecked(isCheck);
                //设置查看的图标
                if ("1".equals(type)) {
                    if (isCheck) {
                        icon = "/qcdwn/js/jQuery/zTree/css/zTreeStyle/img/diy/tick.png";
                    } else {
                        icon = "/qcdwn/js/jQuery/zTree/css/zTreeStyle/img/diy/cross.png";
                    }
                    treeNode.setIcon(icon);
                }
                treeNodeList.add(treeNode);
            }
        }
        return treeNodeList;
    }

    /**
     * 根据系统编号和角色获取对应的角色权限目录数
     *
     * @param sysno
     * @return
     */
    public List<TreeNode> getPrivilegeBySysnoAndRoleId(String sysno, String roleid) {

        List<SystemMenu> moduleList = privilegeRepository.getAllPrivilegeBySysnoAndRoleId(sysno, roleid);
        String root = sysno;
        String rootId = String.valueOf("Root");
        List<TreeNode> treeNodeList = getChildrenTreeNodes(root, rootId, moduleList, null);

        return treeNodeList;
    }

//    /**
//     * 更新角色权限
//     *
//     * @param sysno
//     * @param roleid
//     * @param privilegeList
//     * @return
//     */
//    public int updatePrivilegeByPrivilegeAndRoleId(String sysno, String roleid, List<SysPrivilege> privilegeList) {
//        return privilegeRepository.updatePrivilegeByPrivilegeAndRoleId(sysno, roleid, privilegeList);
//    }
}
