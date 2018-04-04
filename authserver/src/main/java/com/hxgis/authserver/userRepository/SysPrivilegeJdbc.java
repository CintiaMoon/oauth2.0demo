package com.hxgis.authserver.userRepository;


import com.hxgis.authserver.model.SysPrivilege;
import com.hxgis.authserver.model.SystemMenu;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

//import org.apache.commons.collections.map.HashedMap;

/**
 * Created by xuzhuomin on 2017/7/21.
 */
@Repository
public class SysPrivilegeJdbc extends BaseDaoImpl<SysPrivilege, BigDecimal> {


    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * 根据系统编号获取功能模块目录树
     *
     * @param sysno
     * @return
     */
    public List<SystemMenu> getAllModuleBySysno(String sysno) {
        String sql = "SELECT * from SYS_MODULE start WITH MODULENO in (\n" +
                "select MODULENO from SYS_MODULE WHERE SYSNO='" + sysno + "' AND PARENTNO='Root')\n" +
                " CONNECT BY PRIOR MODULENO = PARENTNO ";
        return namedParameterJdbcTemplate.query(sql, new HashedMap(), BeanPropertyRowMapper.newInstance(SystemMenu.class));
    }

    /**
     * 根据系统编号和角色获取对应的角色权限目录数
     *
     * @param sysno
     * @return
     */
    public List<SystemMenu> getAllPrivilegeBySysnoAndRoleId(String sysno, String roleid) {
        String sql = "select a.*,b.PRIVILEGEOPERATION as HasThisRight from (SELECT * from SYS_MODULE start WITH MODULENO in (\n" +
                "select MODULENO from SYS_MODULE WHERE SYSNO='" + sysno + "' AND PARENTNO='Root')\n" +
                " CONNECT BY PRIOR MODULENO = PARENTNO ) a \n" +
                " left join (select * from SYS_PRIVILEGE WHERE PRIVILEGEMASTER='1' AND PRIVILEGEMASTERKEY='" + roleid + "' ) b\n" +
                " ON a.ID = b.PRIVILEGEACCESSKEY";
        return namedParameterJdbcTemplate.query(sql, new HashedMap(), BeanPropertyRowMapper.newInstance(SystemMenu.class));
    }

    public List<SystemMenu> getAllPrivilegeByUserIdAndRoleId(Integer userId, Integer roleId) {
        String sql = "SELECT A .*, b.USERID FROM (SELECT * FROM SYS_PRIVILEGE WHERE ((PRIVILEGEMASTER = 1 AND PRIVILEGEMASTERKEY = '"+roleId+"') " +
                "OR (PRIVILEGEMASTER = 0 AND PRIVILEGEMASTERKEY = '"+userId+"')) AND PRIVILEGEOPERATION = 1) A " +
                "INNER JOIN SYS_USER b ON b.USERID = A .PRIVILEGEMASTERKEY";

        return namedParameterJdbcTemplate.query(sql, new HashedMap(), BeanPropertyRowMapper.newInstance(SystemMenu.class));

    }

    /**
     * 更新角色权限
     *
     * @param sysno
     * @param roleid
     * @param privilegeList
     * @return
     */
    public int updatePrivilegeByPrivilegeAndRoleId(String sysno, String roleid, List<SysPrivilege> privilegeList) {
        String deleteSql = "delete from SYS_PRIVILEGE WHERE PRIVILEGEMASTERKEY = " + roleid;
        int deleteRt = namedParameterJdbcTemplate.update(deleteSql, new HashedMap());
        return this.addList(privilegeList);
    }
}
