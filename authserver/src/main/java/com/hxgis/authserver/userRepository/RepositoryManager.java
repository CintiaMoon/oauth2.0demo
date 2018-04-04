package com.hxgis.authserver.userRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by xuzhuomin on 2017/7/21.
 */
@Component
public abstract class RepositoryManager<T, PK extends Serializable> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected abstract JpaRepository<T, PK> getRepositoryDao();
}