package com.ibra.projecttracker.controller;


import com.ibra.projecttracker.entity.AuditLog;
import com.ibra.projecttracker.service.impl.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    public List<AuditLog> getAuditLogsForEntity(
            @PathVariable String entityType,
            @PathVariable String entityId) {
        return auditLogService.getAuditLogsForEntity(entityType, entityId);
    }

    @GetMapping("/actor/{actorName}")
    public List<AuditLog> getAuditLogsByActor(@PathVariable String actorName) {
        return auditLogService.getAuditLogsByActor(actorName);
    }

    @GetMapping("/action/{actionType}")
    public List<AuditLog> getAuditLogsByActionType(@PathVariable String actionType) {
        return auditLogService.getAuditLogsByActionType(actionType);
    }

    @GetMapping("/recent")
    public List<AuditLog> getRecentAuditLogs(@RequestParam(defaultValue = "50") int limit) {
        return auditLogService.getRecentAuditLogs(limit);
    }
}