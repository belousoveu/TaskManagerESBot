package com.github.belousovea.taskmanageresbot.service;

import com.github.belousovea.taskmanageresbot.model.Memo;
import com.github.belousovea.taskmanageresbot.repository.MemoRepository;
import org.springframework.stereotype.Service;

@Service
public class MemoService {

    private final MemoRepository memoRepository;

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    public void save(Memo newMemo) {
        memoRepository.save(newMemo);
    }
}
