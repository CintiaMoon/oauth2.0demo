package com.hxgis.authserver.controller;

import com.hxgis.authserver.model.SysRole;
import com.hxgis.authserver.model.SysUser;
import com.hxgis.authserver.userRepository.RoleReposirity;
import com.hxgis.authserver.userRepository.UserReposirity;
import com.hxgis.authserver.userRepository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class PageController {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    UserReposirity userReposirity;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    RoleReposirity roleReposirity;

    @GetMapping({"/", "index"})
    public String index(Model model, RedirectAttributes redirectAttributes) {
        String sql = "SELECT DEPARTMENTID,DEPTNAME FROM SYS_DEPARTMENT";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        model.addAttribute("dept", list);
        redirectAttributes.addFlashAttribute("msg", "注册失败，请重试！");
        return "index";
    }

    @PostMapping(value = "/registration")
    public String createNewUser(RedirectAttributes redirectAttributes, Long regisSex, String regisname, String regisspassword, String regisrealname, String regisdeaprtment, String regisphone) {
        SysUser sys_user = userReposirity.findByLoginName(regisname);
        if (sys_user != null) {
            redirectAttributes.addFlashAttribute("msg", "账号名重复，请重新注册");
        } else {
            String sql = "INSERT INTO SYS_USER (DEPARTMENTID, LOGINNAME, LOGINPASSWORD, PHONE, " +
                    "REALNAME, SEX, ENABLED) " +
                    "VALUES (?,?,?,?,?,?,?)";
            int isSuccess = jdbcTemplate.update(sql, new Object[]{regisdeaprtment, regisname, regisspassword, regisphone, regisrealname, regisSex, 1});
            if (isSuccess == 1) {
                sys_user = userReposirity.findByLoginName(regisname);
                Integer userId = sys_user.getUserId();
                SysRole sysRole = roleReposirity.findByRolename("用户");
                String sql2 = "insert into SYS_USERROLE(USERID,ROLEID)  values(?,?)";
                int isSuccess2 = jdbcTemplate.update(sql2, new Object[]{userId, sysRole.getRoleid().intValue()});
                if (isSuccess2 == 1) {
                    redirectAttributes.addFlashAttribute("msg", "恭喜你，注册成功");
                } else {
                    redirectAttributes.addFlashAttribute("msg", "注册失败，请重试！");
                }
            } else {
                redirectAttributes.addFlashAttribute("msg", "注册失败，请重试！");
            }
        }
        return "redirect:/";
    }
}
