package com.unicom.urban.management.common.listener;

import com.unicom.urban.management.common.constant.EventSourceConstant;
import com.unicom.urban.management.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

/**
 * 监听流程启动
 *
 * @author liukai
 */
@Slf4j
@Component
public class EventListenerForStart implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) {
        String eventName = execution.getEventName();
        if (EVENTNAME_START.equals(eventName)) {
            start(execution);
            return;
        }

        if (EVENTNAME_END.equals(eventName)) {
            log.debug("------------------end---------------------");
            return;
        }
        if (EVENTNAME_TAKE.equals(eventName)) {
            take();
            return;
        }
        throw new BusinessException("EventListenerForStart Exception");
    }

    private void take() {
        log.debug("-------------------------------take----------------------------------");
    }

    private void start(DelegateExecution execution) {
        String eventSource = (String) execution.getVariable("eventSource");
        String eventId = execution.getProcessBusinessKey();
        if (EventSourceConstant.SUPERVISE_REPORTING.equals(eventSource)) {
            log.debug("----------------------监督员上报事件开始--------------------------------------");
            return;
        }

        if (EventSourceConstant.ACCEPTANCE_REPORTING.equals(eventSource)) {
            log.debug("----------------------受理员上报事件开始--------------------------------------");
            return;
        }
        throw new BusinessException("未知的上报类型");
    }

}
