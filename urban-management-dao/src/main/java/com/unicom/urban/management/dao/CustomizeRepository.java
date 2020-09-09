package com.unicom.urban.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author liukai
 */
@NoRepositoryBean
public interface CustomizeRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}
