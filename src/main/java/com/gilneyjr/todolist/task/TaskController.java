package com.gilneyjr.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gilneyjr.todolist.util.Utils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity<Object> create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        taskModel.setUserId((UUID) request.getAttribute("userId"));

        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(taskModel.getStartAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Start date should be after current date");
        }

        if (taskModel.getEndAt().isBefore(taskModel.getStartAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("End date should be after start date");
        }

        return ResponseEntity.status(HttpStatus.OK)
            .body(this.taskRepository.save(taskModel));
    }

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request) {
        return this.taskRepository.findByUserId((UUID) request.getAttribute("userId"));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Object> update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID taskId) {
        var task = this.taskRepository.findById(taskId)
            .orElse(null);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Task could not be found by given id");
        }

        if (!task.getUserId().equals((UUID) request.getAttribute("userId"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Task could not be modified by this user");
        }

        Utils.copyNonNullProperties(taskModel, task);

        return ResponseEntity.status(HttpStatus.OK)
            .body(this.taskRepository.save(task));
    }
}
