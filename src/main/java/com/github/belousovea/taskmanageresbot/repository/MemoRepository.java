package com.github.belousovea.taskmanageresbot.repository;

import com.github.belousovea.taskmanageresbot.model.Memo;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MemoRepository extends ElasticsearchRepository<Memo, String> {

    @Query("""
            {
              "bool": {
                "must": [
                  { "term": { "userId": "?0" } },
                  { "range": { "reminderTime": { "gte": "?1" } } }
                ]
              }
            }
            
            """)
    List<Memo> findActiveMemosByUserId(long userId, String currentTime, Sort sort);

    List<Memo> findByReminderTimeBetween(@NotNull LocalDateTime reminderTime, @NotNull LocalDateTime reminderTime2);

}
