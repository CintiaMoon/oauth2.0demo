package com.hxgis.authserver.userRepository;

import com.hxgis.authserver.model.SysRole;
import com.hxgis.authserver.model.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017-08-08.
 */
@Repository
public interface RoleReposirity extends JpaRepository<SysRole, String> {
    SysRole findByRolename(String name);
}
