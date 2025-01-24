package com.github.belousovea.taskmanageresbot.service;

import com.github.belousovea.taskmanageresbot.events.AddedNewMemoEvent;
import com.github.belousovea.taskmanageresbot.model.Memo;
import com.github.belousovea.taskmanageresbot.model.MemoListDto;
import com.github.belousovea.taskmanageresbot.model.Period;
import com.github.belousovea.taskmanageresbot.repository.MemoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class MemoService {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateFormat.date_hour_minute.getPattern());

    private final ApplicationEventPublisher eventPublisher;
    private final MemoRepository memoRepository;
    private final UserService userService;

    public MemoService(ApplicationEventPublisher eventPublisher, MemoRepository memoRepository, UserService userService) {
        this.eventPublisher = eventPublisher;
        this.memoRepository = memoRepository;
        this.userService = userService;
    }

    public void save(Memo newMemo) {
        memoRepository.save(newMemo);
        log.debug("Saved new memo: {}", newMemo);
        eventPublisher.publishEvent(new AddedNewMemoEvent(this, newMemo));
    }

    public MemoListDto getMemoList(long userId) {
        MemoListDto memoListDto = new MemoListDto();
        memoListDto.setUser(userService.getUser(userId));
        long timeOffset = memoListDto.getUser().getTimeOffset();
        Sort sort = Sort.by(Sort.Order.asc("reminderTime"));
        memoListDto.setMemos(memoRepository.findActiveMemosByUserId(userId,
                LocalDateTime.now().format(dateTimeFormatter), sort));
        if (memoListDto.getMemos().isEmpty()) {
            return memoListDto;
        }

        memoListDto.getMemos().forEach(memo -> memo.setReminderTime(memo.getReminderTime().minusMinutes(timeOffset)));
        memoListDto.setNumberOfMemos((int) memoListDto.getMemos()
                .stream()
                .filter(memo -> !memo.isRepetitive())
                .count());
        memoListDto.setNumberOfPeriodicMemos(memoListDto.getMemos().size() - memoListDto.getNumberOfMemos());
        memoListDto.setNearestMemoTime(memoListDto.getMemos().get(0).getReminderTime().toString().replace("T", " "));
        return memoListDto;
    }

    public List<Memo> getCurrentDayMemos(LocalDateTime startTime, LocalDateTime endTime) {
        return memoRepository.findByReminderTimeBetween(startTime, endTime);
    }

    public void createNewRepetitiveMemo(Memo memo) {
        Memo newMemo = Memo.builder()
                .reminderText(memo.getReminderText())
                .period(memo.getPeriod())
                .userId(memo.getUserId())
                .reminderTime(Period.valueOf(memo.getPeriod()).getNextEventTime(memo.getReminderTime()))
                .build();
        log.debug("Created new repetitive memo: {}", newMemo);
        save(newMemo);
    }
}
