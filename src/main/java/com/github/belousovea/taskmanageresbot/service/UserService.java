package com.github.belousovea.taskmanageresbot.service;

import com.github.belousovea.taskmanageresbot.model.User;
import com.github.belousovea.taskmanageresbot.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User saveUser(org.telegram.telegrambots.meta.api.objects.User telegramUser, long offsetInMinutes) {
        User user = User.builder()
                .userId(telegramUser.getId())
                .userName(telegramUser.getUserName())
                .firstName(telegramUser.getFirstName())
                .lastName(telegramUser.getLastName())
                .timeOffset(offsetInMinutes)
                .build();
        return userRepository.save(user);
    }
}
