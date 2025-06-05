package com.ibra.projecttracker.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibra.projecttracker.entity.AuditLog;
import com.ibra.projecttracker.repository.AuditLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public String getCurrentUser() {
        return "SYSTEM";
    }

    public void saveAuditLog(AuditLog auditLog) {
        try {
            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            System.err.println("Failed to save audit log: " + e.getMessage());
        }
    }

    public void logCreate(String entityType, String entityId, Object entity) {
        try {
            JsonNode payload = objectMapper.valueToTree(entity);
            AuditLog log = AuditLog.createLog(entityType, entityId, getCurrentUser(), payload.toString());
            auditLogRepository.save(log);
        } catch (Exception e) {
            log.debug("Failed to save audit log: " + e.getMessage());
            System.err.println("Failed to create audit log: " + e.getMessage());
        }
    }

    public void logUpdate(String entityType, String entityId, Object oldEntity, Object newEntity) {
        try {
            JsonNode oldPayload = objectMapper.valueToTree(oldEntity);
            JsonNode newPayload = objectMapper.valueToTree(newEntity);

            // Create a combined payload showing before/after
            var payloadNode = objectMapper.createObjectNode();
            payloadNode.set("before", oldPayload);
            payloadNode.set("after", newPayload);

            AuditLog log = AuditLog.updateLog(entityType, entityId, getCurrentUser(), payloadNode.toString());
            auditLogRepository.save(log);
        } catch (Exception e) {
            System.err.println("Failed to create audit log: " + e.getMessage());
        }
    }


    public void logDelete(String entityType, String entityId, Object entity) {
        try {
            JsonNode payload = objectMapper.valueToTree(entity);
            AuditLog log = AuditLog.deleteLog(entityType, entityId, getCurrentUser(), payload.toString());
            auditLogRepository.save(log);
        } catch (Exception e) {
            System.err.println("Failed to create audit log: " + e.getMessage());
        }
    }

    public void logRead(String entityType, String entityId) {
        try {
            AuditLog log = AuditLog.readLog(entityType, entityId, getCurrentUser());
            auditLogRepository.save(log);
        } catch (Exception e) {
            System.err.println("Failed to create audit log: " + e.getMessage());
        }
    }

    public void logProjectCreate(Long projectId, Object project) {
        logCreate("PROJECT", projectId.toString(), project);
    }

    public void logProjectUpdate(Long projectId, Object oldProject, Object newProject) {
        logUpdate("PROJECT", projectId.toString(), oldProject, newProject);
    }

    public void logProjectDelete(Long projectId, Object project) {
        logDelete("PROJECT", projectId.toString(), project);
    }

    public void logTaskCreate(Long taskId, Object task) {
        logCreate("TASK", taskId.toString(), task);
    }

    public void logTaskUpdate(Long taskId, Object oldTask, Object newTask) {
        logUpdate("TASK", taskId.toString(), oldTask, newTask);
    }

    public void logTaskDelete(Long taskId, Object task) {
        logDelete("TASK", taskId.toString(), task);
    }

    public void logDeveloperCreate(Long developerId, Object developer) {
        logCreate("DEVELOPER", developerId.toString(), developer);
    }

    public void logDeveloperUpdate(Long developerId, Object oldDeveloper, Object newDeveloper) {
        logUpdate("DEVELOPER", developerId.toString(), oldDeveloper, newDeveloper);
    }

    public void logDeveloperDelete(Long developerId, Object developer) {
        logDelete("DEVELOPER", developerId.toString(), developer);
    }

    // Custom logging for business events
    public void logTaskStatusChange(Long taskId, String oldStatus, String newStatus) {
        try {
            var payload = objectMapper.createObjectNode();
            payload.put("oldStatus", oldStatus);
            payload.put("newStatus", newStatus);
            payload.put("changedAt", LocalDateTime.now().toString());

            AuditLog log = new AuditLog("STATUS_CHANGE", "TASK", taskId.toString(), getCurrentUser(), payload.toString());
            auditLogRepository.save(log);
        } catch (Exception e) {
            System.err.println("Failed to create audit log: " + e.getMessage());
        }
    }

    public void logTaskDeadlineChange(Long taskId, LocalDateTime oldDeadline, LocalDateTime newDeadline) {
        try {
            var payload = objectMapper.createObjectNode();
            payload.put("oldDeadline", oldDeadline.toString());
            payload.put("newDeadline", newDeadline.toString());
            payload.put("changedAt", LocalDateTime.now().toString());

            AuditLog log = new AuditLog("DEADLINE_CHANGE", "TASK", taskId.toString(), getCurrentUser(), payload.toString());
            auditLogRepository.save(log);
        } catch (Exception e) {
            log.error("Failed to create audit log: {}", e.getMessage());
            System.err.println("Failed to create audit log: " + e.getMessage());
        }
    }

    public void logProjectStatusChange(Long projectId, String oldStatus, String newStatus) {
        try {
            var payload = objectMapper.createObjectNode();
            payload.put("oldStatus", oldStatus);
            payload.put("newStatus", newStatus);
            payload.put("changedAt", LocalDateTime.now().toString());

            AuditLog log = new AuditLog("STATUS_CHANGE", "PROJECT", projectId.toString(), getCurrentUser(), payload.toString());
            auditLogRepository.save(log);
        } catch (Exception e) {
            System.err.println("Failed to create audit log: " + e.getMessage());
        }
    }

    public void logProjectDeadlineChange(Long projectId, LocalDateTime oldDeadline, LocalDateTime newDeadline) {
        try {
            var payload = objectMapper.createObjectNode();
            payload.put("oldDeadline", oldDeadline.toString());
            payload.put("newDeadline", newDeadline.toString());
            payload.put("changedAt", LocalDateTime.now().toString());

            AuditLog log = new AuditLog("DEADLINE_CHANGE", "PROJECT", projectId.toString(), getCurrentUser(), payload.toString());
            auditLogRepository.save(log);
        } catch (Exception e) {
            log.error("Failed to create audit log: {}", e.getMessage());
            System.err.println("Failed to create audit log: " + e.getMessage());
        }
    }

    // Query methods
    public List<AuditLog> getAuditLogsForEntity(String entityType, String entityId) {
        return auditLogRepository.findByEntityTypeAndEntityIdOrderByTimestampDesc(entityType, entityId);
    }

    public List<AuditLog> getAuditLogsByActor(String actorName) {
        return auditLogRepository.findByActorNameOrderByTimestampDesc(actorName);
    }

    public List<AuditLog> getAuditLogsByActionType(String actionType) {
        return auditLogRepository.findByActionTypeOrderByTimestampDesc(actionType);
    }

    public List<AuditLog> getRecentAuditLogs(int limit) {
        return auditLogRepository.findAllByOrderByTimestampDesc().stream().limit(limit).toList();
    }
}