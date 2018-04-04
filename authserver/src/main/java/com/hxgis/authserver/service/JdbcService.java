package com.hxgis.authserver.service;

import com.alibaba.fastjson.JSONObject;
import com.hxgis.authserver.model.SysUser;
import com.hxgis.authserver.userRepository.UserReposirity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class JdbcService {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    UserReposirity userReposirity;


    public List<Map<String, Object>> getUserByDepartmentId(String departmentId) {
        String sql = "SELECT USERID,DEPARTMENTID,LOGINNAME,ORDERNUM,PHONE,QQ,REALNAME,REMARK,SEX,TITLE,WEIXINID,WEIXINNAME,ENABLED  FROM SYS_USER WHERE DEPARTMENTID=?";
        return jdbcTemplate.queryForList(sql, new Object[]{departmentId});
    }

    public Map<String, Object> getUserByName(String userName, String queryRoleSQL) {
        Map<String, Object> map = new HashMap<>();
        SysUser sysUser = userReposirity.findByLoginName(userName);
        sysUser.setLoginPassword("******");
        String sex = sysUser.getSex().toString();
        sex = "2".equals(sex) ? "女" : "男";
        String sqlDepart = "select deptname from sys_department where departmentid=" + sysUser.getDepartmentId();
        String deptId = sysUser.getDepartmentId();
        String sql3 = "select deptaddress from SYS_DEPARTMENT where DEPARTMENTID=" + deptId;
        Map<String, Object> adminInfos = jdbcTemplate.queryForMap(sql3);
        String admincode = Objects.equals(adminInfos.get("deptaddress"), null) ? null : adminInfos.get("deptaddress").toString();
        String province = "湖北省";
        String city = "";
        String county = "";
        if (!Objects.equals(admincode, null)) {
            if (!"420000".equals(admincode)) {
                String sql = "select adminname,praadminno,adminno from send_admin where adminno=" + admincode;
                Map<String, Object> adminInfo = jdbcTemplate.queryForMap(sql);
                String paraId = adminInfo.get("praadminno").toString();
                if (!"420000".equals(paraId)) {
                    county = adminInfo.get("adminname").toString();
                    String sql2 = "select adminname,praadminno,adminno from send_admin where adminno=" + paraId;
                    Map<String, Object> adminInfo2 = jdbcTemplate.queryForMap(sql2);
                    city = adminInfo2.get("adminname").toString();
                } else {

                    city = adminInfo.get("adminname").toString();
                }
            }
        }

        Map<String, Object> roleMap = jdbcTemplate.queryForMap(queryRoleSQL, userName);
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("role", roleMap.get("ROLE"));
        jsonObject.put("loginName", sysUser.getLoginName());
        jsonObject.put("departmentId", sysUser.getDepartmentId());
        jsonObject.put("department", jdbcTemplate.queryForMap(sqlDepart).get("deptname"));
        jsonObject.put("province", province);
        jsonObject.put("city", city);
        jsonObject.put("county", county);
        jsonObject.put("sex", sex);
        jsonObject.put("title", sysUser.getTitle());
        jsonObject.put("userId", sysUser.getUserId());
        jsonObject.put("realName", sysUser.getRealName());
        jsonObject.put("phone", sysUser.getPhone());
        jsonObject.put("qq", sysUser.getQq());
        jsonObject.put("weixinId", sysUser.getWeixinId());
        jsonObject.put("weixinName", sysUser.getWeixinName());
        jsonObject.put("orderNum", sysUser.getOrderNum());
        jsonObject.put("remark", sysUser.getRemark());
        jsonObject.put("enabled", sysUser.getEnabled());
        map.put("userInfo", jsonObject);
        return map;
    }
}
