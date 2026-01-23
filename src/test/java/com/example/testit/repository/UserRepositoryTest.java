package com.example.testit.repository;

import com.example.testit.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void testFindUsername(){
        User user = new User();

        user.setUsername("FindMe");

        userRepository.findByUsername("FindMe");
        Mockito.verify(userRepository).findByUsername("FindMe");
    }
}
