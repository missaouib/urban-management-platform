package com.unicom.urban.management.service.event;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 调用工作流
 *
 * @author jiangwen
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class WorkService {
}
