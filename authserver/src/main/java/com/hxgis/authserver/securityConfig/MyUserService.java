//package com.hxgis.authserver.securityConfig;
//
//import com.hxgis.authserver.model.SysPrivilege;
//import com.hxgis.authserver.model.SysUser;
//import com.hxgis.authserver.model.SystemMenu;
//import com.hxgis.authserver.userRepository.SysPrivilegeJdbc;
//import com.hxgis.authserver.userRepository.UserReposirity;
//import com.hxgis.authserver.userRepository.UserRoleRepository;
//import com.yy.example.bean.Permission;
//import com.yy.example.bean.User;
//import com.yy.example.dao.PermissionDao;
//import com.yy.example.dao.UserDao;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * @author Administrator
// */
//@Service
//public class MyUserService implements UserDetailsService {
//    @Autowired
//    UserReposirity userReposirity;
//    @Autowired
//    SysPrivilegeJdbc sysPrivilegeJdbc;
//
//    @Autowired
//    UserRoleRepository userRoleRepository;
//    @Override
//    public UserDetails loadUserByUsername(String userName) { //重写loadUserByUsername 方法获得 userdetails 类型用户
//
//        SysUser user = userReposirity.findByLoginName(userName);
//        if (user != null) {
//            Integer roleId= userRoleRepository.findSysUserroleByUserid(user.getUserId()).getRoleid();
//            List<SystemMenu> permissions = sysPrivilegeJdbc.getAllPrivilegeByUserIdAndRoleId(user.getUserId(),roleId);
//            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//            for (SystemMenu systemMenu : permissions) {
//                if (systemMenu != null && systemMenu.getName()!=null) {
//                    GrantedAuthority grantedAuthority = new MyGrantedAuthority(systemMenu.getPermissionUrl(),systemMenu.getMethod());
//                    grantedAuthorities.add(grantedAuthority);
//                }
//            }
//            user.setGrantedAuthorities(grantedAuthorities);
//            return user;
//        } else {
//            throw new UsernameNotFoundException("admin: " + userName + " do not exist!");
//        }
//    }
//}