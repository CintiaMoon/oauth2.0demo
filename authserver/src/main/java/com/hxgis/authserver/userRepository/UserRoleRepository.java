package com.hxgis.authserver.userRepository;

        import com.hxgis.authserver.model.SysUserrole;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Modifying;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.stereotype.Repository;

        import javax.transaction.Transactional;

/**
 * 所属公司： 华信联创技术工程有限公司
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-08-04 11:17
 */
@Repository
public interface UserRoleRepository extends JpaRepository<SysUserrole, Integer> {
    SysUserrole findSysUserroleByUserid(Integer userId);

//    @Modifying
//    @Query(value = "insert into SYS_USERROLE(USERID,ROLEID) values(?1,?2)", nativeQuery = true)
//    int saveUserRole(Integer i1, Integer i2);
}
