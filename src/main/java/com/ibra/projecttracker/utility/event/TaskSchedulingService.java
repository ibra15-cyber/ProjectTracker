package com.ibra.projecttracker.utility.event;

import com.ibra.projecttracker.entity.TaskAssignment;
import com.ibra.projecttracker.repository.TaskAssignmentRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class TaskSchedulingService {

    private static final Logger logger = LoggerFactory.getLogger(TaskSchedulingService.class);

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private TaskAssignmentRepository taskAssignmentRepository;

    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @Scheduled(cron = "0 */5 * * * *") // Runs every 5 minutes
    public void scanForDueTasks() {
        List<TaskAssignment> futureTasks = taskAssignmentRepository
                .findTaskAssignmentsWithFutureDueDates();

        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        LocalDateTime threshold = now.plusMinutes(10);

        futureTasks.stream()
                .filter(ta -> ta.getTask().getDeadline().isBefore(threshold))
                .filter(ta -> ta.getLastNotifiedAt() == null ||  // Skip if already notified
                        ta.getLastNotifiedAt().isBefore(ta.getTask().getDeadline()))
                .forEach(this::publishTaskDueEvent); // Reuse event logic
    }

    public void scheduleTaskDueNotification(TaskAssignment taskAssignment) {
        if (taskAssignment == null || taskAssignment.getTask() == null) {
            logger.warn("Cannot schedule notification - taskAssignment or task is null");
            return;
        }

        if (taskAssignment.getDeveloper() == null ||
                !StringUtils.hasText(taskAssignment.getDeveloper().getEmail())) {
            logger.warn("Cannot schedule notification - developer or email is null/empty for task: {}",
                    taskAssignment.getTask().getTitle());
            return;
        }

        Long taskId = taskAssignment.getTask().getTaskId();

        // Cancel any existing scheduled task for this task
        cancelScheduledTask(taskId);

        LocalDateTime dueDateTime = taskAssignment.getTask().getDeadline();
        if (dueDateTime != null && dueDateTime.isAfter(LocalDateTime.now())) {
            try {
                Instant triggerTime = dueDateTime.atZone(ZoneId.systemDefault()).toInstant();

                ScheduledFuture<?> scheduledTask = taskScheduler.schedule(
                        () -> publishTaskDueEvent(taskAssignment),
                        triggerTime
                );

                scheduledTasks.put(taskId, scheduledTask);

                logger.info("Scheduled due date notification for task: {} at: {}",
                        taskAssignment.getTask().getTitle(), dueDateTime);

            } catch (Exception e) {
                logger.error("Failed to schedule task due notification for task: {} - Error: {}",
                        taskAssignment.getTask().getTitle(), e.getMessage(), e);
            }
        } else {
            logger.debug("Task {} is either already due or has no deadline set",
                    taskAssignment.getTask().getTitle());
        }
    }

    public void cancelScheduledTask(Long taskId) {
        if (taskId == null) {
            return;
        }

        ScheduledFuture<?> task = scheduledTasks.remove(taskId);
        if (task != null && !task.isDone()) {
            boolean cancelled = task.cancel(false);
            logger.info("Cancelled scheduled notification for task ID: {} - Success: {}", taskId, cancelled);
        }
    }

    private void publishTaskDueEvent(TaskAssignment taskAssignment) {
        try {
            String developerEmail = taskAssignment.getDeveloper().getEmail();

            eventPublisher.publishEvent(
                    new TaskDueEvent(this, taskAssignment, developerEmail)
            );

            logger.info("Published TaskDueEvent for task: {}",
                    taskAssignment.getTask().getTitle());

        } catch (Exception e) {
            logger.error("Failed to publish TaskDueEvent for task: {} - Error: {}",
                    taskAssignment.getTask().getTitle(), e.getMessage(), e);
        } finally {
            // Clean up completed task from scheduled tasks map
            scheduledTasks.remove(taskAssignment.getTask().getTaskId());
        }
    }

    @PostConstruct
    public void rescheduleExistingTasks() {
        try {
            logger.info("Starting to reschedule existing tasks...");

            // Note: This should fetch tasks that have future due dates and are not completed
            // You need to implement this method in your repository
            List<TaskAssignment> pendingTasks = taskAssignmentRepository.findTaskAssignmentsWithFutureDueDates();

            int scheduledCount = 0;
            for (TaskAssignment taskAssignment : pendingTasks) {
                scheduleTaskDueNotification(taskAssignment);
                scheduledCount++;
            }

            logger.info("Rescheduled {} existing tasks", scheduledCount);

        } catch (Exception e) {
            logger.error("Failed to reschedule existing tasks - Error: {}", e.getMessage(), e);
        }
    }

    @PreDestroy
    public void cleanup() {
        logger.info("Cleaning up scheduled tasks...");
        scheduledTasks.values().forEach(task -> {
            if (!task.isDone()) {
                task.cancel(false);
            }
        });
        scheduledTasks.clear();
        logger.info("All scheduled tasks cleaned up");
    }

    // Method to get count of currently scheduled tasks (for monitoring)
    public int getScheduledTaskCount() {
        return scheduledTasks.size();
    }
}
