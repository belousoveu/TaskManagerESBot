package com.github.belousovea.taskmanageresbot.repository;

import com.github.belousovea.taskmanageresbot.model.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserRepository extends ElasticsearchRepository<User, Long> {
}
