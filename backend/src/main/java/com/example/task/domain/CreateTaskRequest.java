package com.example.task.domain;

import com.example.task.domain.entity.TaskPriority;
import java.time.LocalDate;

public record CreateTaskRequest(
        String title,
        String description,
        LocalDate dueDate,
        TaskPriority priority
) {
}
