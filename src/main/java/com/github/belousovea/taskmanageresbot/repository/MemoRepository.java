package com.github.belousovea.taskmanageresbot.repository;

import com.github.belousovea.taskmanageresbot.model.Memo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoRepository extends ElasticsearchRepository<Memo, String> {
}
