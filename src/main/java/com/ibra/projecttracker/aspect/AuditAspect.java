//package com.ibra.projecttracker.aspect;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ibra.projecttracker.annotation.Auditable;
//import com.ibra.projecttracker.entity.AuditLog;
//import com.ibra.projecttracker.service.impl.AuditLogService;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Aspect
//@Component
//public class AuditAspect {
//
//    @Autowired
//    private AuditLogService auditLogService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @AfterReturning(value = "@annotation(auditable)", returning = "result")
//    public void auditMethod(JoinPoint joinPoint, Auditable auditable, Object result) {
//        try {
//            String methodName = joinPoint.getSignature().getName();
//            String action = auditable.action().isEmpty() ? methodName.toUpperCase() : auditable.action();
//            String entityType = auditable.entityType();
//
//            Object[] args = joinPoint.getArgs();
//
//            // Create payload with method arguments and return value
//            Map<String, Object> payloadMap = new HashMap<>();
//
//            if (auditable.logArgs() && args.length > 0) {
//                payloadMap.put("arguments", objectMapper.convertValue(args, Map.class));
//            }
//
//            if (auditable.logReturn() && result != null) {
//                payloadMap.put("returnValue", objectMapper.convertValue(result, Map.class));
//            }
//
//            payloadMap.put("method", methodName);
//            payloadMap.put("className", joinPoint.getTarget().getClass().getSimpleName());
//
//            // Extract entity ID if possible
//            String entityId = extractEntityId(result, args);
//
//            AuditLog log = new AuditLog(action, entityType, entityId,
//                    auditLogService.getCurrentUser(), payloadMap);
//
//            auditLogService.saveAuditLog(log);
//
//        } catch (Exception e) {
//            System.err.println("Failed to create audit log in aspect: " + e.getMessage());
//        }
//    }
//
//    private String extractEntityId(Object result, Object[] args) {
//        // Try to extract ID from result first
//        if (result != null) {
//            try {
//                if (result instanceof com.ibra.projecttracker.entity.Project) {
//                    return ((com.ibra.projecttracker.entity.Project) result).getProjectId().toString();
//                } else if (result instanceof com.ibra.projecttracker.entity.Task) {
//                    return ((com.ibra.projecttracker.entity.Task) result).getTaskId().toString();
//                } else if (result instanceof com.ibra.projecttracker.entity.Developer) {
//                    return ((com.ibra.projecttracker.entity.Developer) result).getDeveloperId().toString();
//                } else if (result instanceof com.ibra.projecttracker.entity.TaskAssignment) {
//                    return ((com.ibra.projecttracker.entity.TaskAssignment) result).getTaskAssignmentId().toString();
//                }
//            } catch (Exception e) {
//                // Continue to try extracting from args
//            }
//        }
//
//        // Try to extract ID from first argument (common pattern for update/delete operations)
//        if (args.length > 0 && args[0] instanceof Long) {
//            return args[0].toString();
//        }
//
//        return "UNKNOWN";
//    }
//}
