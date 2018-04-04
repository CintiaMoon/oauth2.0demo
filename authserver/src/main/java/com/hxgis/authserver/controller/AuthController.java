package com.hxgis.authserver.controller;

import com.hxgis.authserver.model.SysUser;
import com.hxgis.authserver.model.SysUserrole;
import com.hxgis.authserver.model.TreeNode;
import com.hxgis.authserver.service.JdbcService;
import com.hxgis.authserver.service.SysModuleService;
import com.hxgis.authserver.userRepository.UserReposirity;
import com.hxgis.authserver.userRepository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Cici on 2017/7/3.
 */
@RestController
public class AuthController {
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    SysModuleService moduleService;
    @Autowired
    UserReposirity userReposirity;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    JdbcService jdbcService;

    @Value(value = "${queryRoleSQL}")
    String queryRoleSQL;

    @RequestMapping("/me")
    public Principal user(Principal principal) {
//        System.out.println(principal.getName());
//        Map<String, Object> map = new HashMap<>();
//        SysUser sysUser = userReposirity.findByLoginname(principal.getName());
//        Map<String,Object> roleMap = jdbcTemplate.queryForMap(queryRoleSQL,principal.getName());
//        map.put("userInfo", sysUser);
//        map.put("roleInfo", roleMap);
        return principal;


//        //加载当前子系统对应角色所有的权限列表
//        String sysno = "YiTiHua";
//        SysUserrole userrole = userRoleRepository.findSysUserroleByUserid(sysUser.getUserid());
//        List<TreeNode> treeNodeList = moduleService.getPrivilegeBySysnoAndRoleId(sysno, userrole.getRoleid().toString());
//        map.put("privilege", treeNodeList);
//        return map;
    }

    @GetMapping("/user")
    public Map<String, Object> me(Principal principal) {
        return jdbcService.getUserByName(principal.getName(), queryRoleSQL);
    }

    @GetMapping("/userDetail")
    public Map<String, Object> userDetail(Principal principal) {
        return jdbcService.getUserByName(principal.getName(), queryRoleSQL);
    }

    @GetMapping("privilege")
    public Map<String, Object> privilege(Principal principal, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        SysUser sysUser = userReposirity.findByLoginName(principal.getName());
        String sysno = "YiTiHua";
        SysUserrole userrole = userRoleRepository.findSysUserroleByUserid(sysUser.getUserId());
        List<TreeNode> treeNodeList = moduleService.getPrivilegeBySysnoAndRoleId(sysno, userrole.getRoleid().toString());
        map.put("privilege", treeNodeList);
        return map;
    }

    @GetMapping("/aaa")
    public Map dsad(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sessionId", request.getSession().getId());
        map.put("message", request.getSession().getAttribute("map"));
        return map;
    }

    @GetMapping("getUserByDepartmentId")
    public List<Map<String, Object>> getUserByDepartmentId(String departmentId) {
        return jdbcService.getUserByDepartmentId(departmentId);
    }
}
