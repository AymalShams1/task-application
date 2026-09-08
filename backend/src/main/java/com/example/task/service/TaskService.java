package com.example.task.service;

import com.example.task.domain.CreateTaskRequest;
import com.example.task.domain.UpdateTaskRequest;
import com.example.task.domain.entity.Task;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    Task createTask(CreateTaskRequest request);

    List<Task> listTasks();

    Task updateTask(UUID taskId, UpdateTaskRequest request);

    void deleteTask(UUID taskId);
}
