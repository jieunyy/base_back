package com.example.baseback;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.example.baseback.service.CustomOAuth2UserService;

@SpringBootTest
@ActiveProfiles("test")
class BasebackApplicationTests {

    @MockBean
    private CustomOAuth2UserService customOAuth2UserService;

    @Test
    void contextLoads() {
        // Context loading test
    }
}

