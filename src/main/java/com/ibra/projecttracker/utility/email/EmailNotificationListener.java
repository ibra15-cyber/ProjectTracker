package com.ibra.projecttracker.utility.email;

import com.ibra.projecttracker.entity.Task;
import com.ibra.projecttracker.utility.event.TaskDueEvent;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationListener.class);

    @Autowired
    private JavaMailSender mailSender;

    @EventListener
    @Async
    public void handleTaskDue(TaskDueEvent event) {
        try {
            if (event.getDeveloperEmail() == null || event.getDeveloperEmail().trim().isEmpty()) {
                logger.warn("Cannot send email notification - developer email is null or empty for task: {}",
                        event.getTaskAssignment().getTask().getTitle());
                return;
            }

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(event.getDeveloperEmail());
            helper.setSubject("Task Due: " + event.getTaskAssignment().getTask().getTitle());
            helper.setText(buildEmailContent(event.getTaskAssignment().getTask()), true);

            mailSender.send(message);

            logger.info("Due date notification sent successfully for task: {} to: {}",
                    event.getTaskAssignment().getTask().getTitle(),
                    event.getDeveloperEmail());

        } catch (Exception e) {
            logger.error("Failed to send due date notification for task: {} - Error: {}",
                    event.getTaskAssignment().getTask().getTitle(), e.getMessage(), e);
        }
    }

    private String buildEmailContent(Task task) {
        return String.format(
                "<h2>Task Due Date Alert</h2>" +
                        "<p><strong>Task:</strong> %s</p>" +
                        "<p><strong>Due Date:</strong> %s</p>" +
                        "<p>This task is now due. Please ensure all deliverables are completed.</p>" +
                        "<p><strong>Description:</strong> %s</p>",
                task.getTitle(),
                task.getDeadline(),
                task.getDescription() != null ? task.getDescription() : "No description provided"
        );
    }
}
