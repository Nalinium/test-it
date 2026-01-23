package com.example.testit.service;

import com.example.testit.model.Status;
import com.example.testit.model.Task;
import com.example.testit.model.User;
import com.example.testit.repository.TaskRepository;
import com.example.testit.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TaskServiceTestIT {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void test1(){
        Task task = new Task();
        User user = new User();

        task.setId(1L);
        task.setTitle("Task1");
        task.setDescription("Description Task1");
        task.setAssignedUser(user);
        task.setStatus(Status.EN_COURS);

        taskRepository.save(task);
    }
}
