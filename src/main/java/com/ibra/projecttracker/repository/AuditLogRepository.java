package com.ibra.projecttracker.repository;

import com.ibra.projecttracker.entity.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends MongoRepository<AuditLog, String> {

    List<AuditLog> findByEntityTypeAndEntityIdOrderByTimestampDesc(String entityType, String entityId);

    List<AuditLog> findByActorNameOrderByTimestampDesc(String actorName);

    List<AuditLog> findByActionTypeOrderByTimestampDesc(String actionType);

    List<AuditLog> findAllByOrderByTimestampDesc();
}