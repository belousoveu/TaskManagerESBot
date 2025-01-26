package com.github.belousovea.taskmanageresbot.service;

import com.github.belousovea.taskmanageresbot.TestData;
import com.github.belousovea.taskmanageresbot.model.User;
import com.github.belousovea.taskmanageresbot.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Container
    static ElasticsearchContainer container = new ElasticsearchContainer(DockerImageName.parse("elasticsearch:7.17.27"));


    @DynamicPropertySource
    static void properties(final DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.uris", container::getHttpHostAddress);
    }

    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void test_getUser_whenUserExist() {
        User user1 = User.builder().userId(1L).userName("user1").firstName("user1").lastName("user1").timeOffset(0).build();
        User user2 = User.builder().userId(2L).userName("user2").firstName("user2").lastName("user2").timeOffset(0).build();
        User user3 = User.builder().userId(3L).userName("user3").firstName("user3").lastName("user3").timeOffset(0).build();
        userRepository.saveAll(List.of(user1, user2, user3));

        User actual = userService.getUser(1L);

        assertNotNull(actual);
        assertEquals(user1.getUserId(), actual.getUserId());
    }

    @Test
    void test_getUser_whenUserAbsent() {
        User user1 = User.builder().userId(1L).userName("user1").firstName("user1").lastName("user1").timeOffset(0).build();
        User user2 = User.builder().userId(2L).userName("user2").firstName("user2").lastName("user2").timeOffset(0).build();
        User user3 = User.builder().userId(3L).userName("user3").firstName("user3").lastName("user3").timeOffset(0).build();
        userRepository.saveAll(List.of(user1, user2, user3));

        User actual = userService.getUser(4L);

        assertNull(actual);
    }

    @Test
    void test_saveUser() {
        org.telegram.telegrambots.meta.api.objects.User telegramUser = TestData.getMockTelegramUser();
        long offsetMinutes = 60;

        User actual = userService.saveUser(telegramUser, offsetMinutes);

        assertNotNull(actual);
        assertEquals(telegramUser.getId(), actual.getUserId());
        assertEquals(telegramUser.getUserName(), actual.getUserName());
        assertEquals(offsetMinutes, actual.getTimeOffset());

        Iterable<User> actualUsers = userRepository.findAll();
        List<User> userList = StreamSupport.stream(actualUsers.spliterator(), false).toList();
        assertNotNull(actualUsers);
        assertEquals(1, userList.size());
    }

    @Test
    void test_getUsers() {
        User user1 = User.builder().userId(1L).userName("user1").firstName("user1").lastName("user1").timeOffset(0).build();
        User user2 = User.builder().userId(2L).userName("user2").firstName("user2").lastName("user2").timeOffset(0).build();
        User user3 = User.builder().userId(3L).userName("user3").firstName("user3").lastName("user3").timeOffset(0).build();
        userRepository.saveAll(List.of(user1, user2, user3));


        Set<Long> targetSetIds = Set.of(1L, 2L);
        List<User> actualUsers = userService.getUsers(targetSetIds);

        assertNotNull(actualUsers);
        assertEquals(2, actualUsers.size());
        assertTrue(actualUsers.stream().anyMatch(user -> user.getUserId() == user1.getUserId()));
        assertTrue(actualUsers.stream().anyMatch(user -> user.getUserId() == user2.getUserId()));
        assertFalse(actualUsers.stream().anyMatch(user -> user.getUserId() == user3.getUserId()));

    }
}