package com.cts.controller;

import com.cts.dto.TaskDTO;
import com.cts.service.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reporting/tasks")
public class TaskController {

    @Autowired
    private TaskServiceImpl taskService;

    // ADMIN, MANAGER, or DISPATCHER can create tasks
    @PostMapping
    public ResponseEntity<?> createTask(
            @RequestHeader("X-User-Role") String role,
            @RequestBody TaskDTO dto) {

        if (!role.equals("ADMIN") && !role.equals("MANAGER") && !role.equals("DISPATCHER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN, MANAGER, or DISPATCHER can create tasks");
        }
        return new ResponseEntity<>(taskService.createTask(dto), HttpStatus.CREATED);
    }

    // ADMIN and MANAGER can view all tasks
    @GetMapping
    public ResponseEntity<?> getAllTasks(
            @RequestHeader("X-User-Role") String role) {

        if (!role.equals("ADMIN") && !role.equals("MANAGER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN or MANAGER can view all tasks");
        }
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    // Any authenticated user can view tasks assigned to them
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTasksByUser(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long userId) {

        return ResponseEntity.ok(taskService.getTasksByUser(userId));
    }

    // Filter tasks by status: PENDING, IN_PROGRESS, COMPLETED
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getTasksByStatus(
            @RequestHeader("X-User-Role") String role,
            @PathVariable String status) {

        if (!role.equals("ADMIN") && !role.equals("MANAGER") && !role.equals("DISPATCHER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role to filter tasks by status");
        }
        return ResponseEntity.ok(taskService.getTasksByStatus(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    // Any authenticated user can update task status (e.g. driver marking task complete)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id,
            @RequestBody TaskDTO dto) {

        return ResponseEntity.ok(taskService.updateTask(id, dto));
    }

    // Only ADMIN can delete tasks
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN can delete tasks");
        }
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
