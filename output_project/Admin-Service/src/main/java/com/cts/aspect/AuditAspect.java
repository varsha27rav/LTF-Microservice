package com.cts.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cts.dto.AuditLogDTO;
import com.cts.service.AuditFeignClient;

@Aspect
@Component
public class AuditAspect {

    private final AuditFeignClient auditClient;

    public AuditAspect(AuditFeignClient auditClient) {
        this.auditClient = auditClient;
    }

    @AfterReturning("execution(* com.cts.controller..*(..))")
    public void logSuccess(JoinPoint joinPoint) {
        sendLog(joinPoint, "SUCCESS");
    }

    @AfterThrowing("execution(* com.cts.controller..*(..))")
    public void logFailure(JoinPoint joinPoint) {
        sendLog(joinPoint, "FAILED");
    }

    private void sendLog(JoinPoint joinPoint, String status) {

        try {
            String method = joinPoint.getSignature().getName();
            String service = joinPoint.getTarget().getClass().getSimpleName();

            String username = "Anonymous";
            try {
                username = SecurityContextHolder.getContext().getAuthentication().getName();
            } catch (Exception ignored) {}

            AuditLogDTO log = new AuditLogDTO();
            log.setServiceName(service);
            log.setAction(method + " - " + status);
            log.setMethod(joinPoint.getSignature().toShortString());
            log.setUsername(username);
            log.setDetails("Executed " + method);

            auditClient.sendLog(log);

        } catch (Exception e) {
            System.out.println("Audit error: " + e.getMessage());
        }
    }
}