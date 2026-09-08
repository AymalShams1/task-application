package com.example.task.mapper;

import com.example.task.domain.CreateTaskRequest;
import com.example.task.domain.UpdateTaskRequest;
import com.example.task.domain.dto.CreateTaskRequestDto;
import com.example.task.domain.dto.TaskDto;
import com.example.task.domain.dto.UpdateTaskRequestDto;
import com.example.task.domain.entity.Task;

public interface TaskMapper {
    CreateTaskRequest fromDto(CreateTaskRequestDto dto);
    UpdateTaskRequest fromDto(UpdateTaskRequestDto dto);
    TaskDto toDto(Task task);

}
