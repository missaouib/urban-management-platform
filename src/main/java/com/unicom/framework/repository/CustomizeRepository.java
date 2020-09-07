package com.unicom.framework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author liukai
 */
public interface CustomizeRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}
