package com.gilneyjr.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_task")
public class TaskModel {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private UUID userId;
    @Column(length = 50)
    private String title;
    private String description;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;
    @CreationTimestamp
    private LocalDateTime createdAt;


    public void setTitle(String title) throws Exception {
        if (title.length() > 50)
            throw new Exception("Title's length should not be greater than 50 characteres.");
        this.title = title;
    }
}
