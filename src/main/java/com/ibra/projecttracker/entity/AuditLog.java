package com.ibra.projecttracker.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
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
    private JsonNode payload;

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
                    String actorName, JsonNode payload) {
        this(actionType, entityType, entityId, actorName);
        this.payload = payload;
    }

    // Utility methods for common action types
    public static AuditLog createLog(String entityType, String entityId, String actorName, JsonNode payload) {
        return new AuditLog("CREATE", entityType, entityId, actorName, payload);
    }

    public static AuditLog updateLog(String entityType, String entityId, String actorName, JsonNode payload) {
        return new AuditLog("UPDATE", entityType, entityId, actorName, payload);
    }

    public static AuditLog deleteLog(String entityType, String entityId, String actorName, JsonNode payload) {
        return new AuditLog("DELETE", entityType, entityId, actorName, payload);
    }

    public static AuditLog readLog(String entityType, String entityId, String actorName) {
        return new AuditLog("READ", entityType, entityId, actorName);
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
