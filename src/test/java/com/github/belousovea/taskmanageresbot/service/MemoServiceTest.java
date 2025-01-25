package com.github.belousovea.taskmanageresbot.service;

import com.github.belousovea.taskmanageresbot.events.AddedNewMemoEvent;
import com.github.belousovea.taskmanageresbot.model.Memo;
import com.github.belousovea.taskmanageresbot.repository.MemoRepository;
import com.github.belousovea.taskmanageresbot.scheduler.ReminderScheduler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static com.github.belousovea.taskmanageresbot.TestData.mockMemo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@Testcontainers
@SpringBootTest
class MemoServiceTest {

    @MockitoBean
    ApplicationEventPublisher eventPublisher;

    @MockitoBean
    ReminderScheduler reminderScheduler;

    @Autowired
    MemoRepository memoRepository;

    @Autowired
    UserService userService;

    @Autowired
    private MemoService memoService;

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
        memoRepository.deleteAll();
        doNothing().when(reminderScheduler).init();
    }

    @Test
    void save() {
        assertTrue(Mockito.mockingDetails(eventPublisher).isMock());
        assertSame(eventPublisher, memoService.getEventPublisher());

        doNothing().when(eventPublisher).publishEvent(any(AddedNewMemoEvent.class));
        Memo newMemo = mockMemo("test");
        memoService.save(newMemo);

        List<Memo> memos = (List<Memo>) memoRepository.findAll();
        assertEquals(1, memos.size());
        assertEquals("test", memos.get(0).getReminderText());
    }

    @Test
    void getMemoList() {
    }

    @Test
    void getCurrentDayMemos() {
    }

    @Test
    void createNewRepetitiveMemo() {
    }
}