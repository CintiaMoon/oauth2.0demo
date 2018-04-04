package com.hxgis.authserver.userRepository;

import com.hxgis.authserver.model.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by Administrator on 2017-08-08.
 */
@Repository
public interface UserReposirity extends JpaRepository<SysUser, String> {
    SysUser findByLoginName(String loginName);
}
