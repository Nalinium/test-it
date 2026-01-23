package com.example.testit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.testit.adapter.mail.MailService;
import com.example.testit.model.User;
import com.example.testit.repository.TaskRepository;
import com.example.testit.repository.UserRepository;

import java.util.Optional;

public class TaskServiceTest {
    TaskService taskService;
    TaskRepository taskRepository;
    UserRepository userRepository;
    MailService mailService;

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        mailService = Mockito.mock(MailService.class);

        taskService = new TaskService(taskRepository, userRepository, mailService);
    }

    @Test
    void testCreateTask() {
        User requester = new User();
        User assigned = new User();

        requester.setUsername("Requester");
        requester.setId(1L);

        assigned.setUsername("Assigned");
        assigned.setId(2L);
        assigned.setManager(requester);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(requester));
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(assigned));

        taskService.createTask("Task1", "Description Task1", 1L, 2L);

        Mockito.verify(taskRepository).save(Mockito.any());
    }

    @Test
    void testFindAll() {
        taskService.findAll();
        Mockito.verify(taskRepository).findAll();
    }
}
