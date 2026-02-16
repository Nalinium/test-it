package com.example.testit;

import com.example.testit.adapter.user.CurrentUserServiceFake;
import com.example.testit.model.Status;
import com.example.testit.model.Task;
import com.example.testit.model.User;
import com.example.testit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUserServiceFake currentUserServiceFake;

    @Autowired
    PasswordEncoder passwordEncoder;

    private Long userId;

    @BeforeEach
    void setUp() {
        // Créer un utilisateur de test en DB
        User user = new User("testuser");
        user.setPassword(passwordEncoder.encode("tata"));

        userRepository.save(user);
        userId = user.getId();
        // Set current user for auth
        currentUserServiceFake.setCurrent(userId);
    }

    String credentials = "testuser:tata";
    String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes());

    @Test
    void getAllTasks_shouldReturnEmptyList_initially() throws Exception {
        mockMvc.perform(get("/tasks").header("Authorization", "Basic " + base64Credentials))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void createTask_shouldCreateTask() throws Exception {
        String taskJson = """
            {
                "title": "Test Task",
                "description": "Test Description"
            }
            """;

        mockMvc.perform(post("/tasks").header("Authorization", "Basic " + base64Credentials)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.status").value("OUVERT"));
    }

    @Test
    void getTasksByUser_shouldReturnUserTasks() throws Exception {
        mockMvc.perform(get("/tasks/user/{userId}", userId).header("Authorization", "Basic " + base64Credentials))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // Pour startTask et finishTask, faudrait créer d'abord la tâche via API, mais pour simplifier.
}
