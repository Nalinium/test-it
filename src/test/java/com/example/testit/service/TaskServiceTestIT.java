package com.example.testit.service;

import com.example.testit.model.Status;
import com.example.testit.model.Task;
import com.example.testit.model.User;
import com.example.testit.repository.TaskRepository;
import com.example.testit.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TaskServiceTestIT {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskService taskService;

    @Test
    public void test1(){
        Task task = new Task();
        User user = new User();

        user.setId(1L);
        user.setUsername("User");

        task.setId(1L);
        task.setTitle("Task1");
        task.setDescription("Description Task1");
        task.setAssignedUser(user);
        task.setStatus(Status.OUVERT);

        taskRepository.save(task);
        userRepository.save(user);

        taskService.startTask(task.getId(), user.getId());

        Assertions.assertThat(task.getStatus()).isEqualTo(Status.EN_COURS);
    }

    @Test
    public void test2(){
        Task task = new Task();

        task.setId(1L);
        task.setTitle("Task1");

        taskRepository.save(task);
        taskService.deleteTask(task.getId());

        Assertions.assertThat(taskRepository.findAll()).isEqualTo(List.of());
    }
}
