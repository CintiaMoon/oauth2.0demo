package com.hxgis.authserver.userRepository;


import com.hxgis.authserver.model.SysModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by xuzhuomin on 2017/7/21.
 */
@Repository
public interface SysModuleRepository extends JpaRepository<SysModule, BigDecimal> {

    /**
     *
     * @param sysno
     * @return
     */
    List<SysModule> findBySysno(String sysno);

    /**
     *
     * @param sysno
     * @param parentno
     * @return
     */
    List<SysModule> findBySysnoAndParentno(String sysno, String parentno);
}
