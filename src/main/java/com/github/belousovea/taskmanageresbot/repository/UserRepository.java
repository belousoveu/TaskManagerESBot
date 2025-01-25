package com.github.belousovea.taskmanageresbot.repository;

import com.github.belousovea.taskmanageresbot.model.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends ElasticsearchRepository<User, Long> {

    List<User> findByUserIdIn(Set<Long> userIdSet);

}
