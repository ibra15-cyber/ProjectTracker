package com.ibra.projecttracker.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "audit_logs")
public class AuditLog {

    @Id
    private String id;

    @Field("action_type")
    @Indexed
    private String actionType;

    @Field("entity_type")
    @Indexed
    private String entityType;

    @Field("entity_id")
    @Indexed
    private String entityId;

    @Field("timestamp")
    @Indexed
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @Field("actor_name")
    @Indexed
    private String actorName;

    @Field("payload")
    private String payload;

    public AuditLog() {
        this.timestamp = LocalDateTime.now();
    }

    public AuditLog(String actionType, String entityType, String entityId, String actorName) {
        this();
        this.actionType = actionType;
        this.entityType = entityType;
        this.entityId = entityId;
        this.actorName = actorName;
    }

    public AuditLog(String actionType, String entityType, String entityId,
                    String actorName, String payload) {
        this(actionType, entityType, entityId, actorName);
        this.payload = payload;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    // Utility methods for common action types
    public static AuditLog createLog(String entityType, String entityId, String actorName, String payload) {
        return new AuditLog("CREATE", entityType, entityId, actorName, payload);
    }

    public static AuditLog updateLog(String entityType, String entityId, String actorName, String payload) {
        return new AuditLog("UPDATE", entityType, entityId, actorName, payload);
    }

    public static AuditLog deleteLog(String entityType, String entityId, String actorName, String payload) {
        return new AuditLog("DELETE", entityType, entityId, actorName, payload);
    }

    public static AuditLog readLog(String entityType, String entityId, String actorName) {
        return new AuditLog("READ", entityType, entityId, actorName);
    }

    public static AuditLog loginLog(String actorName, String payload) {
        return new AuditLog("LOGIN", "USER", actorName, actorName, payload);
    }

    public static AuditLog logoutLog(String actorName) {
        return new AuditLog("LOGOUT", "USER", actorName, actorName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditLog auditLog = (AuditLog) o;
        return Objects.equals(id, auditLog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "id='" + id + '\'' +
                ", actionType='" + actionType + '\'' +
                ", entityType='" + entityType + '\'' +
                ", entityId='" + entityId + '\'' +
                ", timestamp=" + timestamp +
                ", actorName='" + actorName + '\'' +
                ", payload=" + payload +
                '}';
    }
}
