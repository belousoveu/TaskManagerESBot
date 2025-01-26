package com.github.belousovea.taskmanageresbot.service;

import com.github.belousovea.taskmanageresbot.TestData;
import com.github.belousovea.taskmanageresbot.events.AddedNewMemoEvent;
import com.github.belousovea.taskmanageresbot.model.Memo;
import com.github.belousovea.taskmanageresbot.model.MemoListDto;
import com.github.belousovea.taskmanageresbot.model.Period;
import com.github.belousovea.taskmanageresbot.repository.MemoRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.StreamSupport;

import static com.github.belousovea.taskmanageresbot.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Testcontainers
@SpringBootTest
@ExtendWith(SpringExtension.class)
class MemoServiceTest {




    @MockitoBean
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private MemoRepository memoRepository;

    @MockitoBean
    private UserService userService;

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
        memoService = new MemoService(eventPublisher, memoRepository, userService);
        memoRepository.deleteAll();
    }

    @Test
    void test_save() {

        doNothing().when(eventPublisher).publishEvent(any(AddedNewMemoEvent.class));

        Memo newMemo = mockMemo("test");
        memoService.save(newMemo);

        Iterable<Memo> memos = memoRepository.findAll();
        List<Memo> memoList = StreamSupport.stream(memos.spliterator(), false).toList();

        assertEquals(1, memoList.size());
        assertEquals("test", memoList.get(0).getReminderText());

        verify(eventPublisher, times(1)).publishEvent(any(AddedNewMemoEvent.class));

    }

    @Test
    void test_getMemoList_whenMemosExist() {

        List<Memo> memoList = TestData.getTestMemos();
        memoRepository.saveAll(memoList);

        when(userService.getUser(anyLong())).thenReturn(TEST_USER);

        MemoListDto actual = memoService.getMemoList(TEST_USER.getUserId());

        assertNotNull(actual);
        assertEquals(TEST_USER, actual.getUser());
        assertEquals(3, actual.getMemos().size());
        assertEquals(2, actual.getNumberOfMemos());
        assertEquals(1, actual.getNumberOfPeriodicMemos());
        assertEquals(FIRST_MEMO.getReminderTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                actual.getNearestMemoTime());

    }

    @Test
    void test_getMemoList_whenMemosListIsEmpty() {

        List<Memo> memoList = TestData.getTestMemos();
        memoRepository.saveAll(memoList);

        when(userService.getUser(anyLong())).thenReturn(NEW_USER);

        MemoListDto actual = memoService.getMemoList(NEW_USER.getUserId());

        assertNotNull(actual);
        assertEquals(NEW_USER, actual.getUser());
        assertTrue(actual.getMemos().isEmpty());

    }

    @Test
    void test_getCurrentDayMemos() {
        LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = start.plusDays(1);

        List<Memo> expected = TestData.getTestMemos();
        memoRepository.saveAll(expected);

        List<Memo> actual = memoService.getCurrentDayMemos(start, end);

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());

    }

    @Test
    void test_createNewRepetitiveMemo() {
        doNothing().when(eventPublisher).publishEvent(any(AddedNewMemoEvent.class));
        Memo newMemo = mockDailyMemo("test");

        memoService.createNewRepetitiveMemo(newMemo);

        Iterable<Memo> memos = memoRepository.findAll();
        List<Memo> memoList = StreamSupport.stream(memos.spliterator(), false).toList();

        assertEquals(1, memoList.size());
        assertEquals("test", memoList.get(0).getReminderText());
        assertEquals(Period.DAY.getNextEventTime(newMemo.getReminderTime())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
                memoList.get(0).getReminderTime().toString());
        verify(eventPublisher, times(1)).publishEvent(any(AddedNewMemoEvent.class));
    }
}